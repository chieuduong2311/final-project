package com.student.tkpmnc.finalproject.repository;

import com.student.tkpmnc.finalproject.entity.RawUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<RawUser, Long> {

    @Query(value = "UPDATE user u SET u.is_deleted = true WHERE u.id = :id", nativeQuery = true)
    void deleteCustomerById(Long id);

    Optional<RawUser> findFirstByPhone(String phone);

    Optional<RawUser> findFirstByUsername(String username);
}
