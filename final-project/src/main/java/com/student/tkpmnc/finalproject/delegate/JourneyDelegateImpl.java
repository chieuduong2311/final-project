package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.JourneyApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Journey;
import com.student.tkpmnc.finalproject.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JourneyDelegateImpl implements JourneyApiDelegate {

    @Autowired
    JourneyService journeyService;

    @Override
    public ResponseEntity<Void> assignDriver(String id, String driverId) {
        journeyService.assignDriver(id, driverId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> cancelJourney(String id, String body) {
        journeyService.cancelJourney(id, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Journey> createJourney(Journey body) {
        return new ResponseEntity<>(journeyService.createJourney(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> endJourney(String id) {
        journeyService.endJourney(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> findDriver(String id) {
        journeyService.findDriver(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Journey>> getAllJourneyByUser(String id) {
        return new ResponseEntity<>(journeyService.getAllJourneyByUser(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Journey> getInProgressJourneyByCustomer(String id) {
        return new ResponseEntity<>(journeyService.getInProgressJourneyByCustomerId(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Journey> getInProgressJourneyByDriver(String id) {
        return new ResponseEntity<>(journeyService.getInProgressJourneyByDriverId(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Journey> getJourneyById(String id) {
        return new ResponseEntity<>(journeyService.getJourneyById(id), HttpStatus.OK);
    }
}
