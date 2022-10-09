package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.CustomerApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.Customer;
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
    public ResponseEntity<Customer> createCustomer(Customer body) {
        return new ResponseEntity<>(customerService.createCustomer(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(String id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> getCustomerInfoByPhone(String phone) {
        return new ResponseEntity<>(customerService.getCustomerInfoByPhone(phone), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Place>> getMostPlaces(String id) {
        return new ResponseEntity<>(customerService.getMostPlaces(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Call>> getRecentCalls(String id) {
        return new ResponseEntity<>(customerService.getRecentCalls(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> registerCustomer(Customer body) {
        return new ResponseEntity<>(customerService.createCustomer(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Customer> updateCustomer(String id, Customer body) {
        return new ResponseEntity<>(customerService.updateCustomer(id, body), HttpStatus.OK);
    }
}
