package com.student.tkpmnc.finalproject.entity;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public class RawUser extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "user_type", nullable = false)
    private Integer userType;

    @Column(name = "user_status", nullable = false)
    private Integer userStatus;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
