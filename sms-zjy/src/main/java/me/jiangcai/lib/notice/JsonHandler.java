package me.jiangcai.lib.notice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.AbstractResponseHandler;

import java.io.IOException;

/**
 * @author CJ
 */
public class JsonHandler extends AbstractResponseHandler<JsonNode> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode handleEntity(HttpEntity httpEntity) throws IOException {

        return objectMapper.readTree(httpEntity.getContent());
    }

}
