package com.student.tkpmnc.finalproject.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="goongDistanceMatrixApi", url="${feign.client.url}")
public interface DistanceMatrixApi {

    String apiKey = "V7uLX09gBrrcesY7zZhSUfr9Hb7AcQ3N0YxM2r1v";

    default DistanceResponse getDistanceDefault(String origins, String destinations) {
        return getDistance(origins, destinations, "car", apiKey);
    }

    @RequestMapping(method = RequestMethod.GET)
    DistanceResponse getDistance(@RequestParam(value = "origins") String origins,
                                 @RequestParam(value = "destinations") String destinations,
                                 @RequestParam(value = "vehicle") String vehicle,
                                 @RequestParam(value = "api_key") String apiKey);

}
