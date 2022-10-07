package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.api.model.VehicleType;
import com.student.tkpmnc.finalproject.entity.RawVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<RawVehicle, Long> {
    Optional<RawVehicle> findFirstByDriverIdAndType(Long id, VehicleType type);
    Optional<RawVehicle> findFirstByDriverId(Long id);
}
