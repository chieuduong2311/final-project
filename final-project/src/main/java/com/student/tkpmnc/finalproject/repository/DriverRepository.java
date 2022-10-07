package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<RawDriver, Long> {

    Optional<RawDriver> findFirstByPhone(String phone);

    Optional<RawDriver> findFirstByUsername(String username);

    @Query(value = "UPDATE driver d SET d.is_deleted = true WHERE d.username = :username", nativeQuery = true)
    void deleteCustomerByUsername(String username);
}
