package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends JpaRepository<RawJourney, Long> {
}