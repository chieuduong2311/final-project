package com.student.tkpmnc.finalproject.service.impl;

import com.student.tkpmnc.finalproject.api.model.LoginInfo;
import com.student.tkpmnc.finalproject.repository.CustomerRepository;
import com.student.tkpmnc.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerUserService implements UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    @Transactional
    public String loginUser(LoginInfo body) {
        return null;
    }

    @Override
    @Transactional
    public void logoutUser(String username) {

    }
}
