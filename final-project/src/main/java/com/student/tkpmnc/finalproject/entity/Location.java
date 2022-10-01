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
    @GeneratedValue
    private long id;

    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "place_id")
    private String placeId;

    @Column(name = "times")
    private Integer times;
}
