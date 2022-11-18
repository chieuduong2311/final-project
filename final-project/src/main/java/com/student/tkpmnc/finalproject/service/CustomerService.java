package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.Place;
import com.student.tkpmnc.finalproject.entity.RawCall;
import com.student.tkpmnc.finalproject.entity.RawPlace;
import com.student.tkpmnc.finalproject.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    CallRepository callRepository;

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Transactional
    public List<Place> getMostPlaces(String phone) {
        log.info("getMostPlaces received request: {}", phone);
        var listRawPlace = placeRepository.findFiveMostLocationByPhone(phone);
        log.info("getMostPlaces returned: {}", listRawPlace);
        return listRawPlace.stream().map(RawPlace::toPlace).collect(Collectors.toList());
    }

    @Transactional
    public List<Call> getRecentCalls(String phone) {
        log.info("getRecentCalls received request: {}", phone);
        List<RawCall> rawCallList = callRepository.getFiveRecentCallsByPhone(phone);
        log.info("getRecentCalls returned: {}", rawCallList);
        return rawCallList.stream().map(this::convertToCall).collect(Collectors.toList());
    }

    private Call convertToCall(RawCall rawCall) {
        Call call = new Call();
        var origin = placeRepository.findFirstByPlaceId(rawCall.getOrigin());
        var destination = placeRepository.findFirstByPlaceId(rawCall.getDestination());
        call.id(rawCall.getId())
                .dateTime(rawCall.getDateTime().getTime())
                .customerId(rawCall.getCustomerId())
                .callType(rawCall.getCallType())
                .phone(rawCall.getPhone())
                .vehicleType(rawCall.getVehicleType());
        if (origin.isEmpty() || destination.isEmpty()) {
            return call;
        }
        call.origin(origin.get().toPlace()).destination(destination.get().toPlace());
        return call;
    }
}
