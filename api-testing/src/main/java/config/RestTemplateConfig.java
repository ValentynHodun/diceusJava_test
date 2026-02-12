package config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import logging.ClientRestInterceptor;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClientBuilder;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.pet.invoker.CustomInstantDeserializer;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RestTemplate restTemplate(ClientHttpRequestFactory requestFactory) {
        BufferingClientHttpRequestFactory bufferingRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);
        RestTemplate restTemplate = new RestTemplate(bufferingRequestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof AbstractJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((AbstractJackson2HttpMessageConverter) converter).getObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                ThreeTenModule module = new ThreeTenModule();
                module.addDeserializer(Instant.class, CustomInstantDeserializer.INSTANT);
                module.addDeserializer(OffsetDateTime.class, CustomInstantDeserializer.OFFSET_DATE_TIME);
                module.addDeserializer(ZonedDateTime.class, CustomInstantDeserializer.ZONED_DATE_TIME);
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.registerModule(module);
            }
        }


        // This allows us to read the response more than once - Necessary for debugging.
        return addRestLogging(restTemplate);
    }

    @Bean
    public ClientHttpRequestFactory createRequestFactory() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(100);

        RequestConfig config = RequestConfig.custom().setConnectTimeout(120000).build();
        HttpClient httpClient = CachingHttpClientBuilder.create().setCacheConfig(cacheConfig()).setConnectionManager(connectionManager)
                .setDefaultRequestConfig(config).useSystemProperties().build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestInterceptor = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestInterceptor.setReadTimeout(120000);
        return clientHttpRequestInterceptor;
    }

    @Bean
    public CacheConfig cacheConfig() {
        return CacheConfig
                .custom()
                .setMaxObjectSize(500000) // 500KB
                .setMaxCacheEntries(2000)
                // Set this to false and a response with queryString
                // will be cached when it is explicitly cacheable .setNeverCacheHTTP10ResponsesWithQueryString(false)
                .build();
    }

    @Bean
    public static CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer scopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> scopes = new HashMap<>();
        scopes.put("thread", SimpleThreadScope.class);
        scopeConfigurer.setScopes(scopes);
        return scopeConfigurer;
    }

    public RestTemplate addRestLogging(RestTemplate restTemplate) {
        List<ClientHttpRequestInterceptor> currentInterceptors = restTemplate.getInterceptors();

        if (currentInterceptors == null) {
            currentInterceptors = new ArrayList<>();
        }
        ClientHttpRequestInterceptor newInterceptor = new ClientRestInterceptor();
        currentInterceptors.add(newInterceptor);
        restTemplate.setInterceptors(currentInterceptors);
        return restTemplate;
    }

}
