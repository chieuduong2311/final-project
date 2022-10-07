package com.student.tkpmnc.finalproject.service.impl;

import com.student.tkpmnc.finalproject.api.model.LoginInfo;
import com.student.tkpmnc.finalproject.repository.DriverRepository;
import com.student.tkpmnc.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverUserService implements UserService {

    @Autowired
    DriverRepository driverRepository;

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
