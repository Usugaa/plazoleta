package com.microservicio.trazabilidad.application.handler;

import com.microservicio.trazabilidad.application.dto.EmployeeEfficiencyResponse;
import com.microservicio.trazabilidad.application.dto.OrderEfficiencyResponse;
import com.microservicio.trazabilidad.application.dto.TraceabilityRequest;
import com.microservicio.trazabilidad.application.dto.TraceabilityResponse;

import java.util.List;

public interface ITraceabilityHandler {

    void saveTraceability(TraceabilityRequest traceabilityRequest);
    List<TraceabilityResponse> getTraceabilityByClient(String clientId);
    List<TraceabilityResponse> getTraceabilityByOrder(Long orderId);
    List<TraceabilityResponse> getTraceabilityByClientAndOrder(String clientId, Long orderId);
    List<OrderEfficiencyResponse> getOrdersEfficiency(String restaurantId);
    List<EmployeeEfficiencyResponse> getEmployeesEfficiency(String restaurantId);
}
