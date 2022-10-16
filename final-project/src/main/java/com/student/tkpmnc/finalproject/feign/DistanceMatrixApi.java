package com.student.tkpmnc.finalproject.feign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="goongDistanceMatrixApi", url="${feign.client.url}")
public interface DistanceMatrixApi {

    @Value("${feign.client.apiKey}")
    String apiKey = null;

    @RequestMapping(method = RequestMethod.GET)
    DistanceResponse getDistance(@RequestParam(value = "origins") String origins,
                                 @RequestParam(value = "destinations") String destinations,
                                 @RequestParam(value = "vehicle") String vehicle,
                                 @RequestParam(value = "api_key") String apiKey);

}
