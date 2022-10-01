package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.CustomerApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.Customer;
import com.student.tkpmnc.finalproject.api.model.Place;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDelegateImpl implements CustomerApiDelegate {
    @Override
    public ResponseEntity<Customer> createCustomer(Customer body) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Customer> getCustomerInfoByPhone(String phone) {
        return null;
    }

    @Override
    public ResponseEntity<List<Place>> getMostPlaces(String id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Call>> getRecentCalls(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Customer> updateCustomer(String id, Customer body) {
        return null;
    }
}
