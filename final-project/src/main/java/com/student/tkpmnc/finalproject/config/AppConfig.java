package com.student.tkpmnc.finalproject.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper om() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    @Bean
    public JsonSchemaFactory factory() {
        return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    }

    @Bean
    public ConcurrentHashMap<String, Boolean> saveLocationFlags() {
        ConcurrentHashMap<String, Boolean> flags = new ConcurrentHashMap<>();
        flags.put("isNeeded", Boolean.FALSE);
        flags.put("isAlreadySaved", Boolean.FALSE);
        return flags;
    }

}
