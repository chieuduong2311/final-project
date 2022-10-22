package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Driver;
import com.student.tkpmnc.finalproject.api.model.DriverLocation;
import com.student.tkpmnc.finalproject.entity.RawDriver;
import com.student.tkpmnc.finalproject.entity.RawVehicle;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.service.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.DriverRepository;
import com.student.tkpmnc.finalproject.repository.VehicleRepository;
import com.student.tkpmnc.finalproject.service.dto.DriverLocationFlag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class DriverService {
    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    SchemaHelper schemaHelper;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ConcurrentHashMap<Long, DriverLocationFlag> driverLocationMap;

    private static final String SCHEMA_NAME = "driver";

    @Transactional
    public Driver createDriver(Driver request) {
        if (!schemaHelper.validate(SCHEMA_NAME, request).isEmpty()) {
            throw new RequestException("Invalid driver request");
        }

        if (driverRepository.findFirstByPhone(request.getPhone()).isPresent()) {
            throw new RequestException("This phone is belonged to another driver, invalid request");
        }
        if (driverRepository.findFirstByUsername(request.getUsername()).isPresent()) {
            throw new RequestException("This username is belonged to another driver, invalid request");
        }
        RawDriver rawDriver = RawDriver.builder()
                .personalId(request.getPersonalId())
                .build();
        rawDriver.setEmail(request.getEmail());
        rawDriver.setPhone(request.getPhone());
        rawDriver.setFirstName(request.getFirstName());
        rawDriver.setLastName(request.getLastName());
        rawDriver.setUsername(request.getUsername());
        String pw = bCryptPasswordEncoder.encode(request.getPassword());
        rawDriver.setPassword(pw);
        rawDriver.setIsDeleted(false);
        rawDriver.setUserStatus(1);
        rawDriver.setUserType(1);
        rawDriver.setOnline(false);

        rawDriver = driverRepository.saveAndFlush(rawDriver);

        RawVehicle rawVehicle = RawVehicle.builder()
                .driverId(rawDriver.getId())
                .controlNumber(request.getVehicleInfo().getControlNumber())
                .type(request.getVehicleInfo().getType())
                .build();
        vehicleRepository.saveAndFlush(rawVehicle);

        request.id(rawDriver.getId())
                .userType(rawDriver.getUserType())
                .userStatus(rawDriver.getUserStatus());
        return request;
    }

    @Transactional
    public void deleteDriver(String id) {
        Long idInLong = Long.parseLong(id);
        var rawDriverOpt = driverRepository.findById(idInLong);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }
        rawDriverOpt.get().setIsDeleted(true);
        driverRepository.saveAndFlush(rawDriverOpt.get());
    }

    @Transactional
    public Driver getDriverById(String id) {
        Long idInLong = Long.parseLong(id);
        var rawDriverOpt = driverRepository.findById(idInLong);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }
        Driver driver = rawDriverOpt.get().toDriver();
        var vehicleOpt = vehicleRepository.findFirstByDriverId(rawDriverOpt.get().getId());
        if (vehicleOpt.isEmpty()) {
            throw new NotFoundException("Driver's vehicle is not found");
        }
        driver.vehicleInfo(vehicleOpt.get().toVehicle());
        return driver;
    }

    @Transactional
    public Driver getDriverByPhone(String phone) {
        var rawDriverOpt = driverRepository.findFirstByPhone(phone);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }
        Driver driver = rawDriverOpt.get().toDriver();
        var vehicleOpt = vehicleRepository.findFirstByDriverId(rawDriverOpt.get().getId());
        if (vehicleOpt.isEmpty()) {
            throw new NotFoundException("Driver's vehicle is not found");
        }
        driver.vehicleInfo(vehicleOpt.get().toVehicle());
        return driver;
    }

    @Transactional
    public Driver updateDriver(String id, Driver request) {
        Long idInLong = Long.parseLong(id);
        var rawDriverOpt = driverRepository.findById(idInLong);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }
        if (!schemaHelper.validate(SCHEMA_NAME, request).isEmpty()) {
            throw new RequestException("Invalid driver request");
        }

        if (driverRepository.findFirstByPhone(request.getPhone()).isPresent()) {
            throw new RequestException("This phone is belonged to another driver, invalid request");
        }

        if (driverRepository.findFirstByUsername(request.getUsername()).isPresent()) {
            throw new RequestException("This username is belonged to another driver, invalid request");
        }

        //prevent updating username
        RawDriver rawDriver = rawDriverOpt.get();
        rawDriver.setEmail(request.getEmail());
        rawDriver.setPhone(request.getPhone());
        rawDriver.setFirstName(request.getFirstName());
        rawDriver.setLastName(request.getLastName());
        String pw = DigestUtils.sha256Hex(request.getPassword());
        rawDriver.setPassword(pw);
        rawDriver.setIsDeleted(false);
        rawDriver.setUserStatus(1);
        rawDriver.setUserType(1);

        rawDriver = driverRepository.saveAndFlush(rawDriver);

        var vehicleOpt = vehicleRepository.findFirstByDriverIdAndType(rawDriver.getId(), request.getVehicleInfo().getType());
        if (vehicleOpt.isEmpty()) {
            throw new NotFoundException("Vehicle with given type is not found");
        }
        //prevent updating vehicle type
        RawVehicle rawVehicle = vehicleOpt.get();
        rawVehicle.setControlNumber(request.getVehicleInfo().getControlNumber());
        rawVehicle = vehicleRepository.saveAndFlush(rawVehicle);

        request.id(rawDriver.getId())
                .username(rawDriver.getUsername())
                .vehicleInfo(rawVehicle.toVehicle())
                .userType(rawDriver.getUserType())
                .userStatus(rawDriver.getUserStatus());
        return request;
    }

    @Transactional
    public void switchToOnlineDriver(String id) {
        Long idInLong = Long.parseLong(id);
        var rawDriverOpt = driverRepository.findById(idInLong);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }

        rawDriverOpt.get().setOnline(true);
        driverRepository.saveAndFlush(rawDriverOpt.get());
    }

    @Transactional
    public void switchToOfflineDriver(String id) {
        Long idInLong = Long.parseLong(id);
        var rawDriverOpt = driverRepository.findById(idInLong);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }

        rawDriverOpt.get().setOnline(false);
        driverRepository.saveAndFlush(rawDriverOpt.get());
        if (driverLocationMap.get(idInLong) != null) {
            driverLocationMap.remove(idInLong);
        }
    }

    public void syncLocation(String id, DriverLocation location) {
        Long idInLong = Long.parseLong(id);
        if (driverLocationMap.containsKey(idInLong)) {
            driverLocationMap.get(idInLong).setDriverLocation(location);
            return;
        }
        var flag = DriverLocationFlag.builder()
                .id(idInLong)
                .driverLocation(location)
                .isOnline(true)
                .build();
        driverLocationMap.put(idInLong, flag);
    }

}
