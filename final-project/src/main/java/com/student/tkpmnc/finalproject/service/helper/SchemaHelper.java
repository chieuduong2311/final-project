package com.student.tkpmnc.finalproject.service.helper;

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
import java.util.stream.Collectors;

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

    public void validate(String schemaName, Object request) {
        JsonSchema schema = jsonSchema(schemaName);
        var json = objectMapper.convertValue(request, JsonNode.class);
        var result = schema.validate(json);
        if (!result.isEmpty()) {
            throw new RequestException(schemaName + " " + result.stream().map(ValidationMessage::getMessage).collect(Collectors.joining(",")));
        }
    }
}
