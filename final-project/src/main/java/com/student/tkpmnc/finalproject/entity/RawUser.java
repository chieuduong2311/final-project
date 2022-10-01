package com.student.tkpmnc.finalproject.entity;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public class RawUser extends Auditable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_type")
    private Integer userType;

    @Column(name = "user_status")
    private Integer userStatus;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
