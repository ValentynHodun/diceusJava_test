package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonStringPrettifier {

    public static String prettifyJsonString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object myJson = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(myJson);
        } catch (JsonProcessingException e) {
            return json;
        }
    }
}
