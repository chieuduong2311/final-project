package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.CustomerApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.Place;
import com.student.tkpmnc.finalproject.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDelegateImpl implements CustomerApiDelegate {

    @Autowired
    CustomerService customerService;

    @Override
    public ResponseEntity<List<Place>> getMostPlaces(String phone) {
        return new ResponseEntity<>(customerService.getMostPlaces(phone), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Call>> getRecentCalls(String phone) {
        return new ResponseEntity<>(customerService.getRecentCalls(phone), HttpStatus.OK);
    }
}
