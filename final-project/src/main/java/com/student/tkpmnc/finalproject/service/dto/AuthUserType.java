package com.student.tkpmnc.finalproject.service.dto;

public enum AuthUserType {
    CUSTOMER("customerAuthService"),
    DRIVER("driverAuthService");

    private String details;

    AuthUserType(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
