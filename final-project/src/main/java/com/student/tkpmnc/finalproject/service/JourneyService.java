package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Journey;
import com.student.tkpmnc.finalproject.api.model.JourneyStatus;
import com.student.tkpmnc.finalproject.entity.Location;
import com.student.tkpmnc.finalproject.entity.RawJourney;
import com.student.tkpmnc.finalproject.entity.RawPlace;
import com.student.tkpmnc.finalproject.exception.NoAvailableDriverException;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.feign.DistanceMatrixApi;
import com.student.tkpmnc.finalproject.feign.dto.DistanceResponse;
import com.student.tkpmnc.finalproject.service.dto.DriverBroadcastMessage;
import com.student.tkpmnc.finalproject.service.helper.PlaceHelper;
import com.student.tkpmnc.finalproject.service.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.*;
import com.student.tkpmnc.finalproject.service.dto.DriverLocationFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Value("${feign.client.apiKey}")
    private String API_KEY;

    @Value("${project.config.maxDistance}")
    private Long MAX_DISTANCE;

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

        int countNumberOfDriver = 0;
        for (DriverLocationFlag flag : driverLocationMap.values()) {
            if (!flag.isOnline()) {
                continue;
            }

            if (journeyRepository.findInProgressJourneyByDriverId(flag.getId()).isPresent()) {
                continue;
            }

            String destinationString = String.join(",", flag.getDriverLocation().getLat().toString(), flag.getDriverLocation().getLng().toString());
            DistanceResponse response = distanceMatrixApi.getDistanceDefault(originString, destinationString, API_KEY);
            Long distance = response.getRows().get(0).getElements().get(0).getDistance().getValue();
            if (distance < MAX_DISTANCE) {
                flag.setDistanceValue(distance);
                countNumberOfDriver++;
            } else {
                flag.setDistanceValue(null);
            }
        }

        List<DriverLocationFlag> flags = new ArrayList<>(driverLocationMap.values());
        Collections.sort(flags, Comparator.comparing(DriverLocationFlag::getDistanceValue));

        List<Long> driverIds = new ArrayList<>();

        while (countNumberOfDriver < flags.size() && countNumberOfDriver < 5) {
            driverIds.add(flags.get(countNumberOfDriver).getId());
            countNumberOfDriver++;
        }
        if (countNumberOfDriver == 0) {
            throw new NoAvailableDriverException("Cannot find any available drivers now.");
        }
        var message = DriverBroadcastMessage.builder()
                .drivers(driverIds)
                .journey(toJourney(journey.get()))
                .build();
        template.convertAndSend("/topic/message", message);
    }

    public Journey getInProgressJourneyByCustomerId(String id) {
        var idInLong = Long.parseLong(id);
        var journeyOpt = journeyRepository.findInProgressJourneyByCustomerId(idInLong);
        if (journeyOpt.isEmpty()) {
            return null;
        }
        return toJourney(journeyOpt.get());
    }

    public Journey getInProgressJourneyByDriverId(String id) {
        var idInLong = Long.parseLong(id);
        var journeyOpt = journeyRepository.findInProgressJourneyByDriverId(idInLong);
        if (journeyOpt.isEmpty()) {
            return null;
        }
        return toJourney(journeyOpt.get());
    }
}
