package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.entity.RawUser;

public interface UserService {

    void logoutUser(String username);

    RawUser getUserByUsername(String username);
}
