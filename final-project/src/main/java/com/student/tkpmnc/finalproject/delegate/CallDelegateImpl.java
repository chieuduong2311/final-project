package com.student.tkpmnc.finalproject.delegate;

import com.student.tkpmnc.finalproject.api.CallApiDelegate;
import com.student.tkpmnc.finalproject.api.model.Call;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CallDelegateImpl implements CallApiDelegate {
    @Override
    public ResponseEntity<Call> createCall(Call body) {
        return null;
    }
}
