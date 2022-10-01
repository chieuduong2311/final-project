package com.student.tkpmnc.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.student.tkpmnc.finalproject.api.model.JourneyStatus;
import com.student.tkpmnc.finalproject.api.model.PaymentMethod;
import com.student.tkpmnc.finalproject.api.model.Place;
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
    @GeneratedValue
    private long id;

    @Column(name = "call_id")
    private Long callId;

    private Long customerId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private JourneyStatus status;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "reason")
    private String reason;

    @JsonProperty("start_time")
    private Long startDateTime;

    @JsonProperty("end_time")
    private Long endDateTime;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

}
