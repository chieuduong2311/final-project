package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<RawPlace, Long> {
    Optional<RawPlace> findFirstByPlaceId(String placeId);

    @Query(value = "select p.* from Location l join Place p on l.place_id = p.place_id where l.customer_id = :customerId and p.is_deleted = false order by l.times desc limit 5", nativeQuery = true)
    List<RawPlace> findFiveMostLocationByCustomerId(Long customerId);

    @Query(value = "select p.* from Location l join Place p on l.place_id = p.place_id where l.phone = :phone and p.is_deleted = false order by l.times desc limit 5", nativeQuery = true)
    List<RawPlace> findFiveMostLocationByPhone(String phone);
}
