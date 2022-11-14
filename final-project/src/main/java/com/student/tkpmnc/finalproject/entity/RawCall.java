package com.student.tkpmnc.finalproject.entity;

import com.student.tkpmnc.finalproject.api.model.Call;
import com.student.tkpmnc.finalproject.api.model.CallType;
import com.student.tkpmnc.finalproject.api.model.VehicleType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "call_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CallType callType;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "origin", nullable = false, columnDefinition="varchar(3000)")
    private String origin;

    @Column(name = "destination", nullable = false, columnDefinition="varchar(3000)")
    private String destination;

    @Column(name = "date_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
}
