package com.microservicio.trazabilidad.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraceabilityRequest {

    private Long orderId;
    private String clientId;
    private String clientEmail;
    private String previousStatus;
    private String newStatus;
    private Long employeeId;
    private String employeeEmail;
    private String restaurantId;

}
