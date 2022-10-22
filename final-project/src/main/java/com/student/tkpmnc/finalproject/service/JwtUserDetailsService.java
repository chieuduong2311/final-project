package com.student.tkpmnc.finalproject.service;

import com.student.tkpmnc.finalproject.entity.RawUser;
import com.student.tkpmnc.finalproject.service.dto.AuthUserType;
import com.student.tkpmnc.finalproject.service.impl.CustomerUserServiceImpl;
import com.student.tkpmnc.finalproject.service.impl.DriverUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    CustomerUserServiceImpl customerUserService;

    @Autowired
    DriverUserServiceImpl driverUserService;

    @Autowired
    ConcurrentHashMap<String, Boolean> authServiceConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RawUser user;
        if (authServiceConfig.get(AuthUserType.CUSTOMER.getDetails())) {
            user = customerUserService.getUserByUsername(username);
        } else {
            user = driverUserService.getUserByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }
}
