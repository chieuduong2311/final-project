package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<RawCustomer, Long> {
}
