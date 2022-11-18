package com.student.tkpmnc.finalproject.entity;

import com.student.tkpmnc.finalproject.api.model.User;
import com.student.tkpmnc.finalproject.api.model.UserStatus;
import com.student.tkpmnc.finalproject.api.model.UserType;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Column(name = "is_deleted", nullable = false, columnDefinition="bit(1) default 0")
    private Boolean isDeleted;

    @Column(name = "is_driver", columnDefinition="bit(1) default 0")
    private Boolean isDriver;

    @Column(name = "personal_id")
    private String personalId;

    @Column(name = "overall_rate")
    private Double overallRate;

    @Column(name = "is_online", columnDefinition="bit(1) default 0")
    private boolean isOnline;


    public User toUser() {
        User user = new User();
        user.userStatus(userStatus)
            .userType(userType)
            .id(id)
            .email(email)
            .phone(phone)
            .firstName(firstName)
            .lastName(lastName)
            .personalId(personalId)
            .overallRate(overallRate)
            .isOnline(isOnline)
            .username(username);
        return user;
    }
}
