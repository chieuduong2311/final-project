package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.api.model.LoginInfo;

public interface UserService {

    String loginUser(LoginInfo body);
    void logoutUser(String username);
}
