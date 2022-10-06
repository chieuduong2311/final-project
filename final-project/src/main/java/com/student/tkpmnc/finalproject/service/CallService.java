package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.Place;
import com.student.tkpmnc.finalproject.entity.RawCall;
import com.student.tkpmnc.finalproject.entity.RawPlace;
import com.student.tkpmnc.finalproject.repository.CallRepository;
import com.student.tkpmnc.finalproject.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CallService {
    @Autowired
    CallRepository callRepository;

    @Autowired
    PlaceRepository placeRepository;


    public Call createCall(Call request) {
        savePlaceIfNotExisted(request.getDestination());
        savePlaceIfNotExisted(request.getOrigin());
        RawCall record = RawCall.builder()
                .callType(request.getCallType())
                .customerId(request.getCustomerId())
                .dateTime(new Date())
                .vehicleType(request.getVehicleType())
                .origin(request.getOrigin().getPlaceId())
                .destination(request.getDestination().getPlaceId())
                .phone(request.getPhone())
                .build();
        record = callRepository.save(record);
        request.id(record.getId());
        request.dateTime(record.getDateTime().getTime());
        return request;
    }

    private void savePlaceIfNotExisted(Place place) {
        if (!placeRepository.findFirstByPlaceId(place.getPlaceId()).isPresent()) {
            RawPlace record = RawPlace.builder()
                    .placeId(place.getPlaceId())
                    .lat(place.getLat())
                    .lng(place.getLng())
                    .fullAddressInString(place.getFullAddressInString())
                    .isDeleted(false)
                    .placeName(place.getPlaceName())
                    .build();
            placeRepository.save(record);
        }
    }
}
