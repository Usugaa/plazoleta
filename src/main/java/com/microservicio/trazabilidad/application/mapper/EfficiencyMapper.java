package com.microservicio.trazabilidad.application.mapper;

import com.microservicio.trazabilidad.application.dto.EmployeeEfficiencyResponse;
import com.microservicio.trazabilidad.application.dto.OrderEfficiencyResponse;
import com.microservicio.trazabilidad.domain.constants.OrderConstants;
import com.microservicio.trazabilidad.domain.model.Traceability;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class EfficiencyMapper {

    public List<OrderEfficiencyResponse> toOrderEfficiencyResponseList(List<Traceability> traceabilities) {
        Map<Long, OrderEfficiencyResponse> orderMap = new HashMap<>();

        for (Traceability trace : traceabilities) {
            OrderEfficiencyResponse response = orderMap.computeIfAbsent(trace.getOrderId(), k -> {
                OrderEfficiencyResponse newResponse = new OrderEfficiencyResponse();
                newResponse.setOrderId(k);
                newResponse.setStatus("IN_PROGRESS");
                return newResponse;
            });

            // Si es estado inicial (AVAILABLE)
            if (trace.getPreviousStatus() == null) {
                response.setStartTime(trace.getDate());
            }
            // Si es estado final (DELIVERED)
            if (OrderConstants.DELIVERED.toString().equals(trace.getNewStatus())) {
                response.setEndTime(trace.getDate());
                response.setStatus("COMPLETED");
            }
        }

        // Calcular duración para cada orden
        return orderMap.values().stream()
                .filter(response -> response.getStartTime() != null)
                .map(response -> {
                    LocalDateTime endTime = response.getEndTime() != null ?
                            response.getEndTime() : LocalDateTime.now();
                    response.setEndTime(endTime);
                    response.setDurationInMinutes(
                            ChronoUnit.MINUTES.between(response.getStartTime(), endTime)
                    );
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<EmployeeEfficiencyResponse> toEmployeeEfficiencyResponseList(List<Traceability> traceabilities) {
        Map<Long, EmployeeData> employeeDataMap = new HashMap<>();

        // Agrupar por empleado
        for (Traceability trace : traceabilities) {
            if (trace.getEmployeeId() == null) continue;

            EmployeeData data = employeeDataMap.computeIfAbsent(trace.getEmployeeId(), k -> {
                EmployeeData newData = new EmployeeData();
                newData.employeeId = k;
                newData.employeeEmail = trace.getEmployeeEmail();
                newData.orderStartTimes = new HashMap<>();
                return newData;
            });

            // Registrar tiempos de inicio y fin
            if (OrderConstants.PREPARATION.toString().equals(trace.getNewStatus())) {
                data.orderStartTimes.put(trace.getOrderId(), trace.getDate());
                data.totalOrders++;
            } else if (OrderConstants.DELIVERED.toString().equals(trace.getNewStatus()) &&
                    data.orderStartTimes.containsKey(trace.getOrderId())) {
                LocalDateTime startTime = data.orderStartTimes.get(trace.getOrderId());
                data.totalDuration += ChronoUnit.MINUTES.between(startTime, trace.getDate());
                data.completedOrders++;
            }
        }

        // Convertir a respuestas
        return employeeDataMap.values().stream()
                .map(data -> {
                    EmployeeEfficiencyResponse response = new EmployeeEfficiencyResponse();
                    response.setEmployeeId(data.employeeId);
                    response.setEmployeeEmail(data.employeeEmail);
                    response.setTotalOrders(data.totalOrders);
                    response.setCompletedOrders(data.completedOrders);
                    response.setAverageDurationInMinutes(
                            data.completedOrders > 0 ? data.totalDuration / data.completedOrders : 0
                    );
                    return response;
                })
                .sorted(Comparator.comparing(EmployeeEfficiencyResponse::getAverageDurationInMinutes))
                .collect(Collectors.toList());
    }

    // Clase auxiliar para manejar los datos de empleados
    private static class EmployeeData {
        Long employeeId;
        String employeeEmail;
        Map<Long, LocalDateTime> orderStartTimes;
        long totalOrders = 0;
        long completedOrders = 0;
        long totalDuration = 0;
    }
}
