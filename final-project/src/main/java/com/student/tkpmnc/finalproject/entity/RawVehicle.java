package com.student.tkpmnc.finalproject.entity;

import com.student.tkpmnc.finalproject.api.model.Vehicle;
import com.student.tkpmnc.finalproject.api.model.VehicleType;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicle")
public class RawVehicle extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "control_number", nullable = false)
    private String controlNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VehicleType type;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    public Vehicle toVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.controlNumber(controlNumber)
                .id(id)
                .type(type);
        return vehicle;
    }
}
