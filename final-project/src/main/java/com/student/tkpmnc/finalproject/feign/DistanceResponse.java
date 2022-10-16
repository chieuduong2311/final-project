package com.student.tkpmnc.finalproject.feign;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistanceResponse {
    List<DistanceRow> rows;
}
