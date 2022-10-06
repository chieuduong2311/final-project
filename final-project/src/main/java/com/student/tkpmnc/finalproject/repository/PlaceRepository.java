package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<RawPlace, Long> {
    Optional<RawPlace> findFirstByPlaceId(String placeId);
}
