package com.student.tkpmnc.finalproject.exception;

import lombok.Getter;

@Getter
public class NoAvailableDriverException extends RuntimeException {
    public NoAvailableDriverException(String message) {
        super(message);
    }
}
