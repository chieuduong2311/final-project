package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(value = "select l from Location l join Place p on l.place_id = p.place_id where l.customer_id = :customerId and p.is_deleted = false", nativeQuery = true)
    List<Location> findFiveMostLocationByCustomerId(Long customerId);

}
