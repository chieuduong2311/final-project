package com.student.tkpmnc.finalproject.entity;


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

    @Column(name = "personal_id")
    private String personalId;

    @Column(name = "overall_rate")
    private Double overallRate;

    @Column(name = "vehicle_id")
    private long vehicleId;
}
