package com.student.tkpmnc.finalproject.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.student.tkpmnc.finalproject.exception.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Component
public class SchemaHelper {

    @Autowired
    JsonSchemaFactory jsonSchemaFactory;

    @Autowired
    ObjectMapper objectMapper;

    public JsonSchema jsonSchema(String schemaName) {
        Resource resource = new ClassPathResource(String.format("schema/%s.json", schemaName));
        try {
            InputStream is = resource.getInputStream();
            return jsonSchemaFactory.getSchema(is);
        } catch (IOException e) {
            throw new RequestException("Cannot validate request");
        }
    }

    public Set<ValidationMessage> validate(String schemaName, Object request) {
        JsonSchema schema = jsonSchema(schemaName);
        var json = objectMapper.convertValue(request, JsonNode.class);
        return schema.validate(json);
    }
}
