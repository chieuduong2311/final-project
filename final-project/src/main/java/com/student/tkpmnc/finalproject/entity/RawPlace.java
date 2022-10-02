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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "place_id", nullable = false)
    private String placeId;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "full_address", nullable = false)
    private String fullAddressInString;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
