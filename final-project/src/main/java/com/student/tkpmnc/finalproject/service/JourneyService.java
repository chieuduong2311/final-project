package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Journey;
import com.student.tkpmnc.finalproject.api.model.JourneyStatus;
import com.student.tkpmnc.finalproject.entity.Location;
import com.student.tkpmnc.finalproject.entity.RawJourney;
import com.student.tkpmnc.finalproject.entity.RawPlace;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.feign.DistanceMatrixApi;
import com.student.tkpmnc.finalproject.feign.DistanceResponse;
import com.student.tkpmnc.finalproject.helper.PlaceHelper;
import com.student.tkpmnc.finalproject.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.*;
import com.student.tkpmnc.finalproject.service.dto.DriverLocationFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class JourneyService {

    @Autowired
    CallRepository callRepository;

    @Autowired
    LocationRepository locationRepository;

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

    @Autowired
    ConcurrentHashMap<Long, DriverLocationFlag> driverLocationMap;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private DistanceMatrixApi distanceMatrixApi;

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

        if (!callRepository.existsById(request.getCallId())) {
            throw new NotFoundException("Call is not existed");
        }

        RawJourney journey = RawJourney.builder()
                .callId(request.getCallId())
                .customerId(request.getCustomerId())
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
        var journeyOpt = journeyRepository.findById(Long.parseLong(id));
        if (journeyOpt.isEmpty()) {
            throw new NotFoundException("Journey is not existed");
        }
        RawJourney journey = journeyOpt.get();
        journey.setEndDateTime(new Date().getTime());
        journey.setStatus(JourneyStatus.COMPLETED);
        journeyRepository.saveAndFlush(journey);

        var location = locationRepository.findFirstByCustomerIdAndPlaceId(journey.getCustomerId(), journey.getDestination());
        if (location.isPresent()) {
            Integer time = location.get().getTimes();
            location.get().setTimes(time+1);
            locationRepository.saveAndFlush(location.get());
        } else {
            Location record = Location.builder()
                    .customerId(journey.getCustomerId())
                    .placeId(journey.getDestination())
                    .times(1)
                    .build();
            locationRepository.saveAndFlush(record);
        }
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

    @Transactional
    public void findDriver(String id) {
        var journeyId = Long.parseLong(id);
        var journey = journeyRepository.findById(journeyId);
        if (journey.isEmpty()) {
            throw new NotFoundException("Journey is not existed");
        }
        RawPlace origin = placeRepository.findFirstByPlaceId(journey.get().getOrigin()).orElseThrow(() -> new NotFoundException("Place does not exist"));
        String originString = String.join(",", origin.getLat().toString() ,origin.getLng().toString());

        //generate destination string
        List<DriverLocationFlag> driverLocationFlagList = driverLocationMap.values()
                .stream().filter(driverLocationFlag -> driverLocationFlag.isOnline())
                .collect(Collectors.toList());
        List<String> driverLocationList = driverLocationFlagList.stream()
                .map(flag -> flag.getDriverLocation())
                .map(location -> String.join(",", location.getLat().toString(), location.getLng().toString()))
                .collect(Collectors.toList());
        String destinationString = String.join("|", driverLocationList);
        System.out.println("-------destination string: " + destinationString);
        DistanceResponse response = distanceMatrixApi.getDistanceDefault(originString, destinationString);
        System.out.println("------------distance response" + response.toString());

        int minIdx = 0;
        var listElement = response.getRows().get(0).getElements();
        for (int i = 0; i < listElement.size(); i++) {
            if (listElement.get(i).getDistance().getValue() < listElement.get(minIdx).getDistance().getValue()) {
                minIdx = i;
            }
        }

        String minLocation = driverLocationList.get(minIdx);

        DriverLocationFlag res = driverLocationFlagList.stream().filter(f -> {
            String desString = String.join(",", f.getDriverLocation().getLat().toString(), f.getDriverLocation().getLng().toString());
            return minLocation.equals(desString);
        }).findAny().get();

        journey.get().setDriverId(res.getId());
        template.convertAndSend("/topic/message", toJourney(journey.get()));
    }
}
