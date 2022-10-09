package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findFirstByCustomerIdAndPlaceId(Long customerId, String placeId);

}
