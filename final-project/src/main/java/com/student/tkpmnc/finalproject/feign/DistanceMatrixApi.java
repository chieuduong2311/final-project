package com.student.tkpmnc.finalproject.feign;

import com.student.tkpmnc.finalproject.feign.dto.DistanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="goongDistanceMatrixApi", url="${feign.client.url}")
public interface DistanceMatrixApi {

    String VEHICLE_TYPE = "car";

    default DistanceResponse getDistanceDefault(String origins, String destinations, String apiKey) {
        return getDistance(origins, destinations, VEHICLE_TYPE, apiKey);
    }

    @RequestMapping(method = RequestMethod.GET)
    DistanceResponse getDistance(@RequestParam(value = "origins") String origins,
                                 @RequestParam(value = "destinations") String destinations,
                                 @RequestParam(value = "vehicle") String vehicle,
                                 @RequestParam(value = "api_key") String apiKey);

}
