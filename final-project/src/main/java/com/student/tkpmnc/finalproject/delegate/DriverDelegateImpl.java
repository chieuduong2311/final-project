package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.DriverApiDelegate;
import com.student.tkpmnc.finalproject.api.model.DriverLocation;
import com.student.tkpmnc.finalproject.api.model.User;
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
    public ResponseEntity<Void> registerDriver(String id, User body) {
        driverService.register(id, body);
        return new ResponseEntity<>(HttpStatus.OK);
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
}
