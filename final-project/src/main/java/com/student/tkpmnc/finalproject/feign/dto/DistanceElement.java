package com.student.tkpmnc.finalproject.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistanceElement {
    private String status;
    private Distance distance;
    private Distance duration;
}
