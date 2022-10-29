package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.UserApiDelegate;
import com.student.tkpmnc.finalproject.api.model.LoginInfo;
import com.student.tkpmnc.finalproject.api.model.LoginResponse;
import com.student.tkpmnc.finalproject.api.model.User;
import com.student.tkpmnc.finalproject.entity.RawUser;
import com.student.tkpmnc.finalproject.repository.UserRepository;
import com.student.tkpmnc.finalproject.service.authentication.JwtUserDetailsService;
import com.student.tkpmnc.finalproject.service.UserService;
import com.student.tkpmnc.finalproject.service.authentication.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserDelegateImpl implements UserApiDelegate {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<User> createUser(User body) {
        return new ResponseEntity<>(userService.createUser(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getUserInfoById(String id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getUserInfoByPhone(String phone) {
        return new ResponseEntity<>(userService.getUserByPhone(phone), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginInfo body) {
//        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(body.getUsername());
        var rawUserOpt = userRepository.findFirstByUsername(body.getUsername());
        if (rawUserOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + body.getUsername());
        }
        RawUser rawUser = rawUserOpt.get();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(rawUser.getUsername(), rawUser.getPassword(), new ArrayList<>());
        String token = jwtTokenUtil.generateToken(userDetails);
        var response = new LoginResponse()
                .token(token)
                .id(rawUser.getId())
                .role(rawUser.getUserType().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> logoutUser(String username) {
        return null;
    }

    @Override
    public ResponseEntity<User> registerUser(User body) {
        return new ResponseEntity<>(userService.createUser(body), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> updateUser(String id, User body) {
        return new ResponseEntity<>(userService.updateUser(id, body), HttpStatus.OK);
    }
}
