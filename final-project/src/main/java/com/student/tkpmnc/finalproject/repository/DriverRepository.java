package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<RawDriver, Long> {
}
