package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.UserApiDelegate;
import com.student.tkpmnc.finalproject.api.model.LoginInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDelegateImpl implements UserApiDelegate {

    @Override
    public ResponseEntity<String> loginUser(LoginInfo body) {
        return null;
    }

    @Override
    public ResponseEntity<Void> logoutUser() {
        return null;
    }
}
