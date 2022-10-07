package com.student.tkpmnc.finalproject.entity;


import com.student.tkpmnc.finalproject.api.model.Driver;
import com.student.tkpmnc.finalproject.service.DriverService;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Table(name = "driver")
public class RawDriver extends RawUser {

    @Column(name = "personal_id", nullable = false)
    private String personalId;

    @Column(name = "overall_rate")
    private Double overallRate;

    public Driver toDriver() {
        Driver driver = new Driver();
        driver.userStatus(getUserStatus())
                .userType(getUserType())
                .id(getId())
                .email(getEmail())
                .phone(getPhone())
                .firstName(getFirstName())
                .lastName(getLastName())
                .personalId(personalId)
                .overallRate(overallRate);
        return driver;
    }
}
