package com.student.tkpmnc.finalproject.service.impl;

import com.student.tkpmnc.finalproject.entity.RawUser;
import com.student.tkpmnc.finalproject.repository.CustomerRepository;
import com.student.tkpmnc.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerUserServiceImpl implements UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    @Transactional
    public void logoutUser(String username) {

    }

    @Override
    @Transactional
    public RawUser getUserByUsername(String username) {
        var userOpt = customerRepository.findFirstByUsername(username);
        if (userOpt.isEmpty()) {
            return null;
        }
        return customerRepository.findFirstByUsername(username).get();
    }
}
