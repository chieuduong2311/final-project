package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.UserApiDelegate;
import com.student.tkpmnc.finalproject.api.model.LoginInfo;
import com.student.tkpmnc.finalproject.service.JwtUserDetailsService;
import com.student.tkpmnc.finalproject.service.authentication.JwtTokenUtil;
import com.student.tkpmnc.finalproject.service.dto.AuthUserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserDelegateImpl implements UserApiDelegate {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    ConcurrentHashMap<String, Boolean> authServiceConfig;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Override
    public ResponseEntity<String> loginUser(String type, LoginInfo body) {
        UserDetails userDetails;
        if (AuthUserType.CUSTOMER.name().equalsIgnoreCase(type)) {
            authServiceConfig.put(AuthUserType.CUSTOMER.getDetails(), Boolean.TRUE);
            authServiceConfig.put(AuthUserType.DRIVER.getDetails(), Boolean.FALSE);
        } else {
            authServiceConfig.put(AuthUserType.DRIVER.getDetails(), Boolean.TRUE);
            authServiceConfig.put(AuthUserType.CUSTOMER.getDetails(), Boolean.FALSE);
        }
        userDetails = jwtUserDetailsService.loadUserByUsername(body.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> logoutUser(String username) {
        return null;
    }
}
