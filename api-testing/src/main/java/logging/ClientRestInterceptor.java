package logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Allure;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.JsonStringPrettifier.prettifyJsonString;

public class ClientRestInterceptor implements ClientHttpRequestInterceptor {
    public String statusText;
    public String responseBody;
    public int statusCode;
    public static Map<String, String> statusTextMap = new HashMap<>();
    public static Map<String, String> responseBodyMap = new HashMap<>();
    public static Map<String, Integer> statusCodeMap = new HashMap<>();


    private static Logger logger = Logger.getLogger(ClientRestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws
            IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(new HttpRequestWrapper(request) {
            @Override
            public URI getURI() {
                URI u = super.getURI();
                String encodedQuery = UriUtils.encodeQuery(u.getQuery(), "UTF-8");
                String strictlyEscapedQuery = (encodedQuery == null ? null :
                        StringUtils.replace(encodedQuery, "+", "%2B").replaceFirst("=$", "%3D"));
                return UriComponentsBuilder.fromUri(u)
                        .replaceQuery(strictlyEscapedQuery)
                        .build(true).toUri();
            }
        }, body);

        logResponse(response);
        return response;
    }

    private synchronized void logRequest(HttpRequest request, byte[] body) throws JsonProcessingException {
        URI uri = request.getURI();
        HttpMethod httpMethod = request.getMethod();
        String httpHeaders = headersToString(request.getHeaders());
        String requestBody = prettifyJsonString(new String(body, StandardCharsets.UTF_8));

        logRequest(uri, httpMethod, httpHeaders, clearLogFromBinaryData(requestBody));
    }

    private synchronized void logResponse(ClientHttpResponse response) throws IOException {
        statusCode = response.getRawStatusCode();
        statusText = response.getStatusText();
        responseBody = bodyToString(response.getBody());
        statusCodeMap.put(Thread.currentThread().getName(), statusCode);
        statusTextMap.put(Thread.currentThread().getName(), statusText);
        responseBodyMap.put(Thread.currentThread().getName(), responseBody);

        String responseHeaders = headersToString(response.getHeaders());

        logResponse(statusCode, statusText, responseHeaders, responseBody);
    }

    private String headersToString(HttpHeaders headers) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            builder.append(entry.getKey()).append("=[");
            for (String value : entry.getValue()) {
                builder.append(value).append(",");
            }
            builder.setLength(builder.length() - 1); // Get rid of trailing comma
            builder.append("],");
        }
        builder.setLength(builder.length() - 1); // Get rid of trailing comma
        return builder.toString();
    }

    private String bodyToString(InputStream body) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(body, StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            builder.append(line).append(System.lineSeparator());
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return prettifyJsonString(builder.toString());
    }

    public void logRequest(URI uri, HttpMethod HTTP_Method, String HTTP_Headers, String Request_Body) {
        logger.info(HTTP_Method + " " + uri);
        logger.debug("HTTP Method: " + HTTP_Method);
        logger.debug("HTTP Headers: " + HTTP_Headers);
        logger.info("Request Body:\n" + Request_Body);

        String url = null;
        try {
            url = uri.toURL().getProtocol() + "//" + uri.getHost() + uri.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Allure.step(HTTP_Method + " " + url, (step) -> {
            step.parameter("Request: ", HTTP_Method + " " + uri);
            if (!HTTP_Headers.isEmpty()) {
                Allure.addAttachment("Headers", "application/json", HTTP_Headers);
            }
            if (!Request_Body.isEmpty()) {
                Allure.addAttachment("Request", "application/json", Request_Body);
            }
        });

    }

    public void logResponse(int HTTP_Status_Code, String Status_Text, String HTTP_Headers, String Response_Body) {
        logger.info("HTTP Status Code: " + HTTP_Status_Code);
        logger.debug("Status Text: " + Status_Text);
        logger.debug("HTTP Headers: " + HTTP_Headers);
        logger.info("Response Body:\n" + Response_Body);
        Allure.step(String.format("Received status code: %s", statusCode), (step) -> {
            step.parameter("HTTP Status Code: ", statusCode);
            if (!responseBody.isEmpty()) {
                Allure.addAttachment("Response", "application/json", responseBody);
            }
        });
    }

    public static synchronized Integer getStatusCode() {
        return statusCodeMap.get(Thread.currentThread().getName());
    }

    public static synchronized String getResponseBody() {
        return responseBodyMap.get(Thread.currentThread().getName());
    }

    private String clearLogFromBinaryData(String httpBody) {
        return StringUtils.substringBefore(httpBody, "�");
    }
}
