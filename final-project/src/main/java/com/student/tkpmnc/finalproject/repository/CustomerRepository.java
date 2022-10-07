package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<RawCustomer, Long> {

    @Query(value = "UPDATE customer c SET c.is_deleted = true WHERE c.id = :id", nativeQuery = true)
    void deleteCustomerById(Long id);

    Optional<RawCustomer> findFirstByPhone(String phone);

    Optional<RawCustomer> findFirstByUsername(String username);
}
