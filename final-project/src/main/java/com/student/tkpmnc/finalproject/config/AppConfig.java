package com.student.tkpmnc.finalproject.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.student.tkpmnc.finalproject.api.model.DriverLocation;
import com.student.tkpmnc.finalproject.service.dto.DriverLocationFlag;
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
    public ConcurrentHashMap<Long, DriverLocationFlag> driverLocationMap() {
        ConcurrentHashMap<Long, DriverLocationFlag> map = new ConcurrentHashMap<>();
        DriverLocation location1 = new DriverLocation().lat(Double.parseDouble("10.762690234072316")).lng(Double.parseDouble("106.68232619763863"));
        DriverLocation location2 = new DriverLocation().lat(Double.parseDouble("10.792001295402772")).lng(Double.parseDouble("106.68972400333254"));
        DriverLocationFlag flag1 = DriverLocationFlag.builder()
                .id(1L)
                .isOnline(true)
                .driverLocation(location1)
                .build();
        DriverLocationFlag flag2 = DriverLocationFlag.builder()
                .id(2L)
                .isOnline(true)
                .driverLocation(location2)
                .build();
        map.put(flag1.getId(), flag1);
        map.put(flag2.getId(), flag2);
        return map;
    }

}
