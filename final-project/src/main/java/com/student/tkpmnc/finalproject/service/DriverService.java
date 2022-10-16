package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Driver;
import com.student.tkpmnc.finalproject.api.model.DriverLocation;
import com.student.tkpmnc.finalproject.entity.RawDriver;
import com.student.tkpmnc.finalproject.entity.RawVehicle;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.DriverRepository;
import com.student.tkpmnc.finalproject.repository.VehicleRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    ConcurrentHashMap<String, Boolean> saveLocationFlags;

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
        String pw = DigestUtils.sha256Hex(request.getPassword());
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
    public void deleteDriver(String username) {
        var rawDriverOpt = driverRepository.findFirstByUsername(username);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }
        driverRepository.deleteCustomerByUsername(username);
    }

    @Transactional
    public Driver getDriverByUsername(String username) {
        var rawDriverOpt = driverRepository.findFirstByUsername(username);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }
        Driver driver = rawDriverOpt.get().toDriver();
        var vehicleOpt = vehicleRepository.findFirstByDriverId(rawDriverOpt.get().getId());
        if (vehicleOpt.isEmpty()) {
            throw new NotFoundException("Driver's vehicle is not found");
        }
        driver.vehicleInfo(vehicleOpt.get().toVehicle());
        return  driver;
    }

    @Transactional
    public Driver updateDriver(String username, Driver request) {
        var rawDriverOpt = driverRepository.findFirstByUsername(username);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }
        if (!schemaHelper.validate(SCHEMA_NAME, request).isEmpty()) {
            throw new RequestException("Invalid driver request");
        }

        if (driverRepository.findFirstByPhone(request.getPhone()).isPresent()) {
            throw new RequestException("This phone is belonged to another driver, invalid request");
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
    public void switchToOnlineDriver(String username) {
        var rawDriverOpt = driverRepository.findFirstByUsername(username);
        if (rawDriverOpt.isEmpty()) {
            throw new NotFoundException("Driver is not found");
        }

        rawDriverOpt.get().setOnline(true);
        driverRepository.saveAndFlush(rawDriverOpt.get());
    }

    @Transactional
    public void syncLocation(String username, DriverLocation location) {
        if (saveLocationFlags.get("isNeeded")) {
            var rawDriverOpt = driverRepository.findFirstByUsername(username);
            if (rawDriverOpt.isEmpty()) {
                throw new NotFoundException("Driver is not found");
            }

            rawDriverOpt.get().setCurrentLat(location.getLat());
            rawDriverOpt.get().setCurrentLng(location.getLng());
            driverRepository.saveAndFlush(rawDriverOpt.get());
//            saveLocationFlags.put("isAlreadySaved", Boolean.TRUE);
        }
    }

}
