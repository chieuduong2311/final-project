package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<RawCall, Long> {

    @Query(value = "select * from call_record c where c.customerId = :customerId order by id desc limit 5", nativeQuery = true)
    List<RawCall> getFiveRecentCallsByCustomerId(Long customerId);
}
