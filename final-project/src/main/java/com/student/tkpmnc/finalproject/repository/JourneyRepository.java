package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JourneyRepository extends JpaRepository<RawJourney, Long> {

    @Query(value = "select * from journey j where j.driver_id = :driverId and j.status = 'INPROGRESS'", nativeQuery = true)
    Optional<RawJourney> findInProgressJourneyByDriverId(Long driverId);

    @Query(value = "select * from journey j where j.customer_id = :customerId and j.status = 'INPROGRESS'", nativeQuery = true)
    Optional<RawJourney> findInProgressJourneyByCustomerId(Long customerId);
}
