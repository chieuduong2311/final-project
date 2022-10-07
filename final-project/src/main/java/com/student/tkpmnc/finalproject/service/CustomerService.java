package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.Customer;
import com.student.tkpmnc.finalproject.api.model.Place;
import com.student.tkpmnc.finalproject.entity.Location;
import com.student.tkpmnc.finalproject.entity.RawCall;
import com.student.tkpmnc.finalproject.entity.RawCustomer;
import com.student.tkpmnc.finalproject.exception.NotFoundException;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.helper.SchemaHelper;
import com.student.tkpmnc.finalproject.repository.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    CallRepository callRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    SchemaHelper schemaHelper;

    private static final String SCHEMA_NAME = "customer";

    @Transactional
    public Customer createCustomer(Customer request) {
        if (!schemaHelper.validate(SCHEMA_NAME, request).isEmpty()) {
            throw new RequestException("Invalid customer request");
        }

        if (customerRepository.findFirstByPhone(request.getPhone()).isPresent()) {
            throw new RequestException("This phone is belonged to another customer, invalid request");
        }

        if (customerRepository.findFirstByUsername(request.getUsername()).isPresent()) {
            throw new RequestException("This username is belonged to another customer, invalid request");
        }

        RawCustomer rawCustomer = RawCustomer.builder()
                .build();
        rawCustomer.setIsDriver(driverRepository.findFirstByPhone(request.getPhone()).isPresent());
        rawCustomer.setEmail(request.getEmail());
        rawCustomer.setPhone(request.getPhone());
        rawCustomer.setFirstName(request.getFirstName());
        rawCustomer.setLastName(request.getFirstName());
        String pw = DigestUtils.sha256Hex(request.getPassword());
        rawCustomer.setPassword(pw);
        rawCustomer.setUserStatus(1);
        rawCustomer.setUserType(1);
        rawCustomer.setIsDeleted(false);
        rawCustomer.setUsername(request.getUsername());

        rawCustomer = customerRepository.saveAndFlush(rawCustomer);
        request.id(rawCustomer.getId())
                .userType(rawCustomer.getUserType())
                .userStatus(rawCustomer.getUserStatus());
        return request;
    }

    @Transactional
    public Customer getCustomerInfoByPhone(String phone) {
        var customerOpt = customerRepository.findFirstByPhone(phone);
        if (customerOpt.isEmpty()) {
            throw new NotFoundException("Customer is not existed");
        }
        RawCustomer rawCustomer = customerOpt.get();
        Customer customer = new Customer();
        customer.userStatus(rawCustomer.getUserStatus())
                .id(rawCustomer.getId())
                .userType(rawCustomer.getUserType())
                .email(rawCustomer.getEmail())
                .firstName(rawCustomer.getFirstName())
                .lastName(rawCustomer.getLastName())
                .phone(rawCustomer.getPhone());
        return customer;
    }

    @Transactional
    public List<Place> getMostPlaces(String id) {
        return locationRepository.findFiveMostLocationByCustomerId(Long.parseLong(id));
    }

    @Transactional
    public List<Call> getRecentCalls(String id) {
        List<RawCall> rawCallList = callRepository.getFiveRecentCallsByCustomerId(Long.parseLong(id));
        return rawCallList.stream().map(this::convertToCall).collect(Collectors.toList());
    }

    @Transactional
    public Customer updateCustomer(String id, Customer request) {
        if (!schemaHelper.validate(SCHEMA_NAME, request).isEmpty()) {
            throw new RequestException("Invalid customer request");
        }

        var customerOpt = customerRepository.findById(Long.parseLong(id));
        if (customerOpt.isEmpty()) {
            throw new NotFoundException("Customer is not existed");
        }
        if (customerRepository.findFirstByPhone(request.getPhone()).isPresent()) {
            throw new RequestException("This phone is belonged to another customer, invalid request");
        }
        if (customerRepository.findFirstByUsername(request.getUsername()).isPresent()) {
            throw new RequestException("This username is belonged to another customer, invalid request");
        }

        RawCustomer rawCustomer = customerOpt.get();
        rawCustomer.setEmail(request.getEmail());
        rawCustomer.setPhone(request.getPhone());
        rawCustomer.setFirstName(request.getFirstName());
        rawCustomer.setLastName(request.getFirstName());
        String pw = DigestUtils.sha256Hex(request.getPassword());
        rawCustomer.setPassword(pw);
        rawCustomer.setUserStatus(1);
        rawCustomer.setUserType(1);
        rawCustomer.setIsDeleted(false);
        rawCustomer.setUsername(request.getUsername());

        rawCustomer = customerRepository.saveAndFlush(rawCustomer);
        request.userType(rawCustomer.getUserType()).userStatus(rawCustomer.getUserStatus());
        return request;
    }

    @Transactional
    public void deleteCustomer(String id) {
        customerRepository.deleteCustomerById(Long.parseLong(id));
    }

    private Call convertToCall(RawCall rawCall) {
        Call call = new Call();
        var origin = placeRepository.findFirstByPlaceId(rawCall.getOrigin());
        var destination = placeRepository.findFirstByPlaceId(rawCall.getDestination());
        if (origin.isEmpty() || destination.isEmpty()) {
            throw new NotFoundException("Origin or destination is not existed");
        }
        call.origin(origin.get().toPlace())
                .destination(destination.get().toPlace())
                .id(rawCall.getId())
                .dateTime(rawCall.getDateTime().getTime())
                .customerId(rawCall.getCustomerId())
                .callType(rawCall.getCallType())
                .phone(rawCall.getPhone())
                .vehicleType(rawCall.getVehicleType());
        return call;
    }
}
