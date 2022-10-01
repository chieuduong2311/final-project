package com.student.tkpmnc.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.Place;
import com.student.tkpmnc.finalproject.api.model.VehicleType;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "call")
public class RawCall extends Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "phone")
    private String phone;


    @Column(name = "call_type")
    private String callType;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "dateTime")
    private Long dateTime;
}
