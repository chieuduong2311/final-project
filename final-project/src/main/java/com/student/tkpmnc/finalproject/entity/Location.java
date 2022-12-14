package com.student.tkpmnc.finalproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "customer_id", nullable = false)
    private long customerId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "place_id", nullable = false, columnDefinition="varchar(3000)")
    private String placeId;

    @Column(name = "times")
    private Integer times;
}
