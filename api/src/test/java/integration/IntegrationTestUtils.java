package integration;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.charset.Charset;

public class IntegrationTestUtils {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    public static MockHttpServletRequestBuilder doPost(String resource, Object object) throws Exception {
        return MockMvcRequestBuilders.post(resource)
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(object));
    }

    public static MockHttpServletRequestBuilder doPut(String resource, Object object) throws Exception {
        return MockMvcRequestBuilders.put(resource)
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(object));
    }

    private static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
