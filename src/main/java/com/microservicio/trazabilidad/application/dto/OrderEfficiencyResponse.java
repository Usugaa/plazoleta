package com.microservicio.trazabilidad.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEfficiencyResponse {

    private Long orderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationInMinutes;
    private String status;

}
