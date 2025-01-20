package com.microservicio.restaurant.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEfficiencyResponse {
    private Long employeeId;
    private String employeeEmail;
    private Long averageDurationInMinutes;
    private Long totalOrders;
    private Long completedOrders;
}
