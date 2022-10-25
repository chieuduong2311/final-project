package com.student.tkpmnc.finalproject.service.dto;

import com.student.tkpmnc.finalproject.api.model.DriverLocation;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocationFlag {
    private Long id;
    private boolean isOnline;
    private DriverLocation driverLocation;
    private Long distanceValue;
}
