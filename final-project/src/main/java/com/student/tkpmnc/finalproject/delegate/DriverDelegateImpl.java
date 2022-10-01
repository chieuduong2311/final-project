package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.DriverApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Driver;
import com.student.tkpmnc.finalproject.api.model.DriverResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DriverDelegateImpl implements DriverApiDelegate {
    @Override
    public ResponseEntity<DriverResponse> createDriver(Driver body) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteDriver(String username) {
        return null;
    }

    @Override
    public ResponseEntity<DriverResponse> getDriverByUsername(String username) {
        return null;
    }

    @Override
    public ResponseEntity<Driver> updateDriver(String username, Driver body) {
        return null;
    }
}
