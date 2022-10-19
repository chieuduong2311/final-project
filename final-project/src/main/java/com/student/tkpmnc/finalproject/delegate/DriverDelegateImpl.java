package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.DriverApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Driver;
import com.student.tkpmnc.finalproject.api.model.DriverLocation;
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
    public ResponseEntity<Void> deleteDriver(String id) {
        driverService.deleteDriver(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Driver> getDriverInfoById(String id) {
        return new ResponseEntity<>((driverService.getDriverById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Driver> getDriverInfoByPhone(String phone) {
        return new ResponseEntity<>(driverService.getDriverByPhone(phone), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Driver> registerDriver(Driver body) {
        return new ResponseEntity<>(driverService.createDriver(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> switchToOfflineDriver(String id) {
        driverService.switchToOfflineDriver(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> switchToOnlineDriver(String id) {
        driverService.switchToOnlineDriver(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> syncLocation(String id, DriverLocation body) {
        driverService.syncLocation(id, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Driver> updateDriver(String id, Driver body) {
        return new ResponseEntity<>(driverService.updateDriver(id, body), HttpStatus.OK);
    }
}
