package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.DriverApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Driver;
import com.student.tkpmnc.finalproject.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DriverDelegateImpl implements DriverApiDelegate {

    @Autowired
    DriverService driverService;

    @Override
    public ResponseEntity<Driver> createDriver(Driver body) {
        return new ResponseEntity<>(driverService.createDriver(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteDriver(String username) {
        return null;
    }

    @Override
    public ResponseEntity<Driver> getDriverByUsername(String username) {
        return new ResponseEntity<>(driverService.getDriverByUsername(username), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Driver> registerDriver(Driver body) {
        return new ResponseEntity<>(driverService.createDriver(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Driver> updateDriver(String username, Driver body) {
        return new ResponseEntity<>(driverService.updateDriver(username, body), HttpStatus.OK);
    }
}
