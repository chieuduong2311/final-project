package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.DriverLocation;
import com.student.tkpmnc.finalproject.api.model.User;
import com.student.tkpmnc.finalproject.api.model.UserType;
import com.student.tkpmnc.finalproject.entity.RawVehicle;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.repository.UserRepository;
import com.student.tkpmnc.finalproject.service.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.VehicleRepository;
import com.student.tkpmnc.finalproject.service.dto.DriverLocationFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class DriverService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    SchemaHelper schemaHelper;

    @Autowired
    ConcurrentHashMap<Long, DriverLocationFlag> driverLocationMap;

    @Value("${project.config.maxDistance}")
    private Long MAX_DISTANCE;

    private static final String SCHEMA_NAME = "driver";

    @Transactional
    public void register(String id, User request) {
        schemaHelper.validate(SCHEMA_NAME, request);
        Long idInLong = Long.parseLong(id);
        var rawUserOpt = userRepository.findById(idInLong);
        if (rawUserOpt.isEmpty()) {
            throw new NotFoundException("User is not found");
        }

        if (vehicleRepository.findFirstByControlNumber(request.getVehicleInfo().getControlNumber()).isPresent()) {
            throw new RequestException("This vehicle is used by another driver, invalid request");
        }

        RawVehicle rawVehicle = RawVehicle.builder()
                .driverId(idInLong)
                .controlNumber(request.getVehicleInfo().getControlNumber())
                .type(request.getVehicleInfo().getType())
                .build();
        vehicleRepository.saveAndFlush(rawVehicle);

        rawUserOpt.get().setIsDriver(true);
        rawUserOpt.get().setUserType(UserType.DRIVER);
        rawUserOpt.get().setPersonalId(request.getPersonalId());
        userRepository.saveAndFlush(rawUserOpt.get());
    }

    @Transactional
    public void switchToOnlineDriver(String id) {
        Long idInLong = Long.parseLong(id);
        var rawDriverOpt = userRepository.findById(idInLong);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }

        rawDriverOpt.get().setOnline(true);
        userRepository.saveAndFlush(rawDriverOpt.get());
    }

    @Transactional
    public void switchToOfflineDriver(String id) {
        Long idInLong = Long.parseLong(id);
        var rawDriverOpt = userRepository.findById(idInLong);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }

        rawDriverOpt.get().setOnline(false);
        userRepository.saveAndFlush(rawDriverOpt.get());
        if (driverLocationMap.get(idInLong) != null) {
            driverLocationMap.remove(idInLong);
        }
    }

    public void syncLocation(String id, DriverLocation location) {
        Long idInLong = Long.parseLong(id);
        if (driverLocationMap.containsKey(idInLong)) {
            driverLocationMap.get(idInLong).setDriverLocation(location);
            driverLocationMap.get(idInLong).setOnline(true);
            driverLocationMap.get(idInLong).setDistanceValue(MAX_DISTANCE);
            return;
        }
        var flag = DriverLocationFlag.builder()
                .id(idInLong)
                .driverLocation(location)
                .isOnline(true)
                .distanceValue(MAX_DISTANCE)
                .build();
        driverLocationMap.put(idInLong, flag);
    }

}
