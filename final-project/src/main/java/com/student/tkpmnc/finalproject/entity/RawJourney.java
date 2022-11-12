package com.student.tkpmnc.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.student.tkpmnc.finalproject.api.model.JourneyStatus;
import com.student.tkpmnc.finalproject.api.model.PaymentMethod;
import com.student.tkpmnc.finalproject.api.model.VehicleType;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "journey")
public class RawJourney extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "call_id")
    private Long callId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JourneyStatus status;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "phone")
    private String phone;

    @JsonProperty("start_time")
    private Long startDateTime;

    @JsonProperty("end_time")
    private Long endDateTime;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "point_code")
    private String pointCode;

}
