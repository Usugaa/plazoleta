package com.microservicio.trazabilidad.infraestructure.output.mongodb.mapper;

import com.microservicio.trazabilidad.domain.model.Traceability;
import com.microservicio.trazabilidad.infraestructure.output.mongodb.entity.TraceabilityEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TraceabilityEntityMapper {

    public TraceabilityEntity toEntity(Traceability traceability) {
        TraceabilityEntity entity = new TraceabilityEntity();
        entity.setOrderId(traceability.getOrderId());
        entity.setClientId(traceability.getClientId());
        entity.setClientEmail(traceability.getClientEmail());
        entity.setDate(traceability.getDate());
        entity.setPreviousStatus(traceability.getPreviousStatus());
        entity.setNewStatus(traceability.getNewStatus());
        entity.setEmployeeId(traceability.getEmployeeId());
        entity.setEmployeeEmail(traceability.getEmployeeEmail());
        entity.setRestaurantId(traceability.getRestaurantId());
        return entity;
    }

    public Traceability toTraceability(TraceabilityEntity entity) {
        return new Traceability.TraceabilityBuilder()
                .orderId(entity.getOrderId())
                .clientId(entity.getClientId())
                .clientEmail(entity.getClientEmail())
                .status(entity.getPreviousStatus(), entity.getNewStatus())
                .employee(entity.getEmployeeId(), entity.getEmployeeEmail())
                .restaurantId(entity.getRestaurantId())
                .build();
    }

    public List<Traceability> toTraceabilityList(List<TraceabilityEntity> entityList) {
        return entityList.stream()
                .map(this::toTraceability)
                .collect(Collectors.toList());
    }
}
