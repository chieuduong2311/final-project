package com.student.tkpmnc.finalproject.service.impl;

import com.student.tkpmnc.finalproject.entity.RawUser;
import com.student.tkpmnc.finalproject.repository.DriverRepository;
import com.student.tkpmnc.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverUserServiceImpl implements UserService {

    @Autowired
    DriverRepository driverRepository;

    @Override
    @Transactional
    public void logoutUser(String username) {

    }

    @Override
    public RawUser getUserByUsername(String username) {
        var userOpt = driverRepository.findFirstByUsername(username);
        if (userOpt.isEmpty()) {
            return null;
        }
        return driverRepository.findFirstByUsername(username).get();
    }
}
