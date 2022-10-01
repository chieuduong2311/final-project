package com.student.tkpmnc.finalproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "place")
public class RawPlace {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "place_id")
    private String placeId;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "name")
    private String name;

    @Column(name = "full_address")
    private String fullAddressInString;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
