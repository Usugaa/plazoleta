package com.microservicio.trazabilidad.application.mapper;

import com.microservicio.trazabilidad.application.dto.TraceabilityRequest;
import com.microservicio.trazabilidad.application.dto.TraceabilityResponse;
import com.microservicio.trazabilidad.domain.model.Traceability;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TraceabilityMapper {

    public Traceability toTraceability(TraceabilityRequest traceabilityRequest){
        return new Traceability.TraceabilityBuilder()
                .orderId(traceabilityRequest.getOrderId())
                .clientId(traceabilityRequest.getClientId())
                .clientEmail(traceabilityRequest.getClientEmail())
                .status(traceabilityRequest.getPreviousStatus(), traceabilityRequest.getNewStatus())
                .employee(traceabilityRequest.getEmployeeId(),traceabilityRequest.getEmployeeEmail())
                .restaurantId(traceabilityRequest.getRestaurantId())
                .build();
    }

    public TraceabilityResponse toResponse(Traceability traceability) {
        TraceabilityResponse traceabilityResponse = new TraceabilityResponse();
        traceabilityResponse.setId(traceability.getId());
        traceabilityResponse.setOrderId(traceability.getOrderId());
        traceabilityResponse.setClientId(traceability.getClientId());
        traceabilityResponse.setClientEmail(traceability.getClientEmail());
        traceabilityResponse.setDate(traceability.getDate());
        traceabilityResponse.setPreviousStatus(traceability.getPreviousStatus());
        traceabilityResponse.setNewStatus(traceability.getNewStatus());
        traceabilityResponse.setEmployeeId(traceability.getEmployeeId());
        traceabilityResponse.setEmployeeEmail(traceability.getEmployeeEmail());
        traceabilityResponse.setRestaurantId(traceability.getRestaurantId());
        return traceabilityResponse;
    }

    public List<TraceabilityResponse> toResponseList(List<Traceability> traceabilityList) {
        return traceabilityList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
