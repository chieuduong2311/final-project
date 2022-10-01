package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.JourneyApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Journey;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class JourneyDelegateImpl implements JourneyApiDelegate {
    @Override
    public ResponseEntity<Void> assignDriver(String id, String driverId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> cancelJourney(String id, String body) {
        return null;
    }

    @Override
    public ResponseEntity<Journey> createJourney(Journey body) {
        return null;
    }

    @Override
    public ResponseEntity<Void> endJourney(String id) {
        return null;
    }
}
