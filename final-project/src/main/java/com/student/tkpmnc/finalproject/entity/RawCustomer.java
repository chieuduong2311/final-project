package com.student.tkpmnc.finalproject.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class RawCustomer extends RawUser {
    @Column(name = "is_driver")
    private Boolean isDriver;
}
