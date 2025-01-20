package com.microservicio.trazabilidad.domain.api;

import com.microservicio.trazabilidad.domain.model.Traceability;

import java.util.List;


public interface ITraceabilityServicePort {
    void saveTraceability(Traceability traceability);
    List<Traceability> getTraceabilityByClient(String clientId);
    List<Traceability> getTraceabilityByOrder(Long orderId);
    List<Traceability> getTraceabilityByClientAndOrder(String clientId, Long orderId);
    List<Traceability> getOrdersEfficiency(String restaurantId);
    List<Traceability> getEmployeesEfficiency(String restaurantId);
}