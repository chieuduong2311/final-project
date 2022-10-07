package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Journey;
import com.student.tkpmnc.finalproject.api.model.JourneyStatus;
import com.student.tkpmnc.finalproject.entity.RawJourney;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.helper.PlaceHelper;
import com.student.tkpmnc.finalproject.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class JourneyService {

    @Autowired
    CallRepository callRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    JourneyRepository journeyRepository;

    @Autowired
    SchemaHelper schemaHelper;

    @Autowired
    PlaceHelper placeHelper;

    private static final String SCHEMA_NAME = "journey";

    @Transactional
    public Journey createJourney(Journey request) {
        if (!schemaHelper.validate(SCHEMA_NAME, request).isEmpty()) {
            throw new RequestException("Invalid journey request");
        }
        placeHelper.savePlaceIfNotExisted(request.getDestination());
        placeHelper.savePlaceIfNotExisted(request.getOrigin());

        if (!customerRepository.existsById(request.getCustomerId())) {
            throw new NotFoundException("Customer is not existed");
        }

        RawJourney journey = RawJourney.builder()
                .callId(request.getCallId())
                .customerId(request.getCustomerId())
                .driverId(request.getDriverId())
                .destination(request.getDestination().getPlaceId())
                .origin(request.getOrigin().getPlaceId())
                .status(JourneyStatus.INPROGRESS)
                .startDateTime(new Date().getTime())
                .price(request.getPrice())
                .paymentMethod(request.getPaymentMethod())
                .build();

        journey = journeyRepository.saveAndFlush(journey);
        request.status(JourneyStatus.INPROGRESS)
                .id(journey.getId())
                .startDateTime(journey.getStartDateTime());
        return request;
    }

    @Transactional
    public void assignDriver(String id, String driverId) {
        var driverIdInLong = Long.parseLong(driverId);
        var journey = journeyRepository.findById(Long.parseLong(id));
        if (journey.isEmpty()) {
            throw new NotFoundException("Journey is not existed");
        }

        if (!driverRepository.existsById(driverIdInLong)) {
            throw new NotFoundException("Driver is not existed");
        }

        journey.get().setDriverId(driverIdInLong);
        journeyRepository.saveAndFlush(journey.get());

    }

    @Transactional
    public void cancelJourney(String id, String reason) {
        var journey = journeyRepository.findById(Long.parseLong(id));
        if (journey.isEmpty()) {
            throw new NotFoundException("Journey is not existed");
        }

        if (journey.get().getDriverId() == null) {
            journey.get().setStatus(JourneyStatus.CANCELED);
        } else {
            journey.get().setStatus(JourneyStatus.CORRUPTED);
        }
        journey.get().setReason(reason);
        journey.get().setEndDateTime(new Date().getTime());
        journeyRepository.saveAndFlush(journey.get());
    }

    @Transactional
    public void endJourney(String id) {
        var journey = journeyRepository.findById(Long.parseLong(id));
        if (journey.isEmpty()) {
            throw new NotFoundException("Journey is not existed");
        }
        journey.get().setEndDateTime(new Date().getTime());
        journey.get().setStatus(JourneyStatus.COMPLETED);
        journeyRepository.saveAndFlush(journey.get());
    }

    @Transactional
    public Journey getJourneyById(String id) {
        var journeyId = Long.parseLong(id);
        var journey = journeyRepository.findById(journeyId);
        if (journey.isEmpty()) {
            throw new NotFoundException("Journey is not existed");
        }
        return toJourney(journey.get());
    }

    private Journey toJourney(RawJourney rawJourney) {
        Journey journey = new Journey();
        var origin = placeRepository.findFirstByPlaceId(rawJourney.getOrigin());
        var destination = placeRepository.findFirstByPlaceId(rawJourney.getDestination());
        if (origin.isEmpty() || destination.isEmpty()) {
            throw new NotFoundException("Origin or destination is not existed");
        }
        journey.destination(destination.get().toPlace())
                .origin(origin.get().toPlace())
                .customerId(rawJourney.getCustomerId())
                .callId(rawJourney.getCallId())
                .driverId(rawJourney.getDriverId())
                .endDateTime(rawJourney.getEndDateTime())
                .id(rawJourney.getId())
                .status(rawJourney.getStatus())
                .rate(rawJourney.getRate())
                .reason(rawJourney.getReason())
                .price(rawJourney.getPrice())
                .paymentMethod(rawJourney.getPaymentMethod());
        return journey;
    }
}
