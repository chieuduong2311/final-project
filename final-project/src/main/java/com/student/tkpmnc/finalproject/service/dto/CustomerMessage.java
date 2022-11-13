package com.student.tkpmnc.finalproject.service.dto;

import com.student.tkpmnc.finalproject.api.model.User;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMessage {
    private Long customerId;
    private String message;
    private CustomerMessageType type;
    private User driver;
}
