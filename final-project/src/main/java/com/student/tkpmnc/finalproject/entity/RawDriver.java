package com.student.tkpmnc.finalproject.entity;


import com.student.tkpmnc.finalproject.api.model.Driver;
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

    @Column(name = "is_online", columnDefinition="bit(1) default 0")
    private boolean isOnline;

    @Column(name = "current_lat")
    private Double currentLat;

    @Column(name = "current_lng")
    private Double currentLng;

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
