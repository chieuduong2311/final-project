package com.student.tkpmnc.finalproject.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerMessageType {

    SUCCESS_FINISH("SUCCESS_FINISH"),

    SUCCESS_START("SUCCESS_START");


    private String value;

    CustomerMessageType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static CustomerMessageType fromValue(String text) {
        for (CustomerMessageType b : CustomerMessageType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
