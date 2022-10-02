package com.student.tkpmnc.finalproject.entity;

import com.student.tkpmnc.finalproject.api.model.CallType;
import com.student.tkpmnc.finalproject.api.model.VehicleType;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "call_record")
public class RawCall extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "call_type")
    @Enumerated(EnumType.STRING)
    private CallType callType;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "date_time")
    private Long dateTime;
}
