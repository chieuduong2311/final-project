package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.entity.RawCall;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.service.helper.PlaceHelper;
import com.student.tkpmnc.finalproject.service.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.CallRepository;
import com.student.tkpmnc.finalproject.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CallService {
    @Autowired
    CallRepository callRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    SchemaHelper schemaHelper;

    @Autowired
    PlaceHelper placeHelper;

    private static final String SCHEMA_NAME = "call";

    @Transactional
    public Call createCall(Call request) {
        if (!schemaHelper.validate(SCHEMA_NAME, request).isEmpty()) {
            throw new RequestException("Invalid request - missing some required fields");
        }

        placeHelper.savePlaceIfNotExisted(request.getDestination());
        placeHelper.savePlaceIfNotExisted(request.getOrigin());
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
}
