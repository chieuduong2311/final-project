package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.CallApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.exception.RequestException;
import com.student.tkpmnc.finalproject.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CallDelegateImpl implements CallApiDelegate {
    @Autowired
    CallService callService;

    @Override
    public ResponseEntity<Call> createCall(Call body) {
        return new ResponseEntity<>(callService.createCall(body), HttpStatus.OK);
    }
}
