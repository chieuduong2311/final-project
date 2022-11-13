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
import com.student.tkpmnc.finalproject.service.dto.CustomerMessage;
import com.student.tkpmnc.finalproject.service.dto.CustomerMessageType;
import com.student.tkpmnc.finalproject.service.dto.DriverBroadcastMessage;
import com.student.tkpmnc.finalproject.service.helper.PlaceHelper;
import com.student.tkpmnc.finalproject.service.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.*;
import com.student.tkpmnc.finalproject.service.dto.DriverLocationFlag;
import com.student.tkpmnc.finalproject.service.socketio.server.JourneyModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JourneyService {

    @Autowired
    CallRepository callRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    JourneyRepository journeyRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    SchemaHelper schemaHelper;

    @Autowired
    PlaceHelper placeHelper;

    @Autowired
    ConcurrentHashMap<Long, DriverLocationFlag> driverLocationMap;

    @Autowired
    JourneyModule journeyModule;

    @Autowired
    private DistanceMatrixApi distanceMatrixApi;

    @Value("${feign.client.apiKey}")
    private String API_KEY;

    @Value("${project.config.maxDistance}")
    private Long MAX_DISTANCE;

    private static final String SCHEMA_NAME = "journey";

    @Transactional
    public Journey createJourney(Journey request) {
        schemaHelper.validate(SCHEMA_NAME, request);
        placeHelper.savePlaceIfNotExisted(request.getDestination());
        placeHelper.savePlaceIfNotExisted(request.getOrigin());

        if (!userRepository.existsById(request.getCustomerId())) {
            throw new NotFoundException("Customer is not existed");
        }

//        if (!callRepository.existsById(request.getCallId())) {
//            throw new NotFoundException("Call is not existed");
//        }

        RawJourney journey = RawJourney.builder()
                .callId(request.getCallId())
                .customerId(request.getCustomerId())
                .destination(request.getDestination().getPlaceId())
                .origin(request.getOrigin().getPlaceId())
                .vehicleType(request.getVehicleType())
                .status(JourneyStatus.INITIALIZED)
                .startDateTime(new Date().getTime())
                .phone(request.getPhone())
                .price(request.getPrice())
                .paymentMethod(request.getPaymentMethod())
                .build();

        journey = journeyRepository.saveAndFlush(journey);
        request.status(JourneyStatus.INITIALIZED)
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


        if (journey.get().getDriverId() != null && !journey.get().getStatus().equals(JourneyStatus.INITIALIZED)) {
            throw new RequestException("Journey has been accepted by another driver!");
        }

        var driverOpt = userRepository.findById(driverIdInLong);
        var vehicleOpt = vehicleRepository.findFirstByDriverId(driverIdInLong);

        if (driverOpt.isEmpty() || vehicleOpt.isEmpty()) {
            throw new NotFoundException("Driver or vehicle is not existed");
        }

        var driver = driverOpt.get().toUser();
        driver.setVehicleInfo(vehicleOpt.get().toVehicle());

        journey.get().setDriverId(driverIdInLong);
        journey.get().setStatus(JourneyStatus.INPROGRESS);
        journeyRepository.saveAndFlush(journey.get());

        CustomerMessage message = CustomerMessage.builder()
                .message("Enjoy your journey. Please contact admin hotline 1800 0000 if any issues!")
                .customerId(journey.get().getCustomerId())
                .driver(driver)
                .type(CustomerMessageType.SUCCESS_START)
                .build();
        journeyModule.sendEvent(message, "customers-noti");
    }

    @Transactional
    public void cancelJourney(String id, String reason) {
        var journey = journeyRepository.findById(Long.parseLong(id));
        if (journey.isEmpty()) {
            throw new NotFoundException("Journey is not existed");
        }

        if (journey.get().getDriverId() == null) {
            journey.get().setStatus(JourneyStatus.CANCELLED);
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
                    .phone(journey.getPhone())
                    .times(1)
                    .build();
            locationRepository.saveAndFlush(record);
        }

        CustomerMessage message = CustomerMessage.builder()
                .message("Journey finished. Thank you for using our service!")
                .customerId(journey.getCustomerId())
                .type(CustomerMessageType.SUCCESS_FINISH)
                .build();
        journeyModule.sendEvent(message, "finish-journey");
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
        journey.customerId(rawJourney.getCustomerId())
                .callId(rawJourney.getCallId())
                .driverId(rawJourney.getDriverId())
                .vehicleType(rawJourney.getVehicleType())
                .endDateTime(rawJourney.getEndDateTime())
                .phone(rawJourney.getPhone())
                .id(rawJourney.getId())
                .status(rawJourney.getStatus())
                .rate(rawJourney.getRate())
                .reason(rawJourney.getReason())
                .price(rawJourney.getPrice())
                .paymentMethod(rawJourney.getPaymentMethod());
        if (origin.isEmpty() || destination.isEmpty()) {
            return journey;
        }
        journey.origin(origin.get().toPlace()).destination(destination.get().toPlace());
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
        String originString = String.join(",", origin.getLat().toString(), origin.getLng().toString());

        int countNumberOfDriver = 0;
        for (DriverLocationFlag flag : driverLocationMap.values()) {
            if (!flag.isOnline()) {
                flag.setDistanceValue(MAX_DISTANCE);
                continue;
            }

            var vehicleInfo = vehicleRepository.findFirstByDriverId(flag.getId());
            if (vehicleInfo.isEmpty()) {
                flag.setDistanceValue(MAX_DISTANCE);
                continue;
            }

            if (!journey.get().getVehicleType().equals(vehicleInfo.get().getType())) {
                flag.setDistanceValue(MAX_DISTANCE);
                continue;
            }

            String destinationString = String.join(",", flag.getDriverLocation().getLat().toString(), flag.getDriverLocation().getLng().toString());
            DistanceResponse response = distanceMatrixApi.getDistanceDefault(originString, destinationString, API_KEY);
            Long distance = response.getRows().get(0).getElements().get(0).getDistance().getValue();
            flag.setDistanceValue(distance);
        }

        List<DriverLocationFlag> flags = new ArrayList<>(driverLocationMap.values());
        Collections.sort(flags, Comparator.comparing(DriverLocationFlag::getDistanceValue));

        List<Long> driverIds = new ArrayList<>();

        while (countNumberOfDriver < flags.size() && countNumberOfDriver < 5) {
            if (flags.get(countNumberOfDriver).getDistanceValue() < MAX_DISTANCE) {
                driverIds.add(flags.get(countNumberOfDriver).getId());
            }
            countNumberOfDriver++;
        }
//        if (countNumberOfDriver == 0) {
//            throw new NoAvailableDriverException("Cannot find any available drivers now.");
//        }
        var message = DriverBroadcastMessage.builder()
                .drivers(driverIds)
                .journey(toJourney(journey.get()))
                .build();
        journeyModule.sendEvent(message, "drivers");
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
