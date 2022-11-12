package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.Place;
import com.student.tkpmnc.finalproject.entity.RawCall;
import com.student.tkpmnc.finalproject.entity.RawPlace;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.service.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    CallRepository callRepository;

    @Transactional
    public List<Place> getMostPlaces(String phone) {
        var listRawPlace = placeRepository.findFiveMostLocationByPhone(phone);
        return listRawPlace.stream().map(RawPlace::toPlace).collect(Collectors.toList());
    }

    @Transactional
    public List<Call> getRecentCalls(String phone) {
        List<RawCall> rawCallList = callRepository.getFiveRecentCallsByPhone(phone);
        return rawCallList.stream().map(this::convertToCall).collect(Collectors.toList());
    }

    private Call convertToCall(RawCall rawCall) {
        Call call = new Call();
        var origin = placeRepository.findFirstByPlaceId(rawCall.getOrigin());
        var destination = placeRepository.findFirstByPlaceId(rawCall.getDestination());
        if (origin.isEmpty() || destination.isEmpty()) {
            throw new NotFoundException("Origin or destination is not existed");
        }
        call.origin(origin.get().toPlace())
                .destination(destination.get().toPlace())
                .id(rawCall.getId())
                .dateTime(rawCall.getDateTime().getTime())
                .customerId(rawCall.getCustomerId())
                .callType(rawCall.getCallType())
                .phone(rawCall.getPhone())
                .vehicleType(rawCall.getVehicleType());
        return call;
    }
}
