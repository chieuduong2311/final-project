package com.student.tkpmnc.finalproject.entity;

import com.student.tkpmnc.finalproject.api.model.VehicleType;

import javax.persistence.*;

@Entity
@Table(name = "vehicle")
public class RawVehicle {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "control_number")
    private String controlNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private VehicleType type;

    @Column(name = "driver_id")
    private long driverId;
}
