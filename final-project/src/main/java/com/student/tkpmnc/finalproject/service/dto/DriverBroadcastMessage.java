package com.student.tkpmnc.finalproject.service.dto;

import com.student.tkpmnc.finalproject.api.model.Journey;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverBroadcastMessage {
    private Journey journey;
    private List<Long> drivers;
}
