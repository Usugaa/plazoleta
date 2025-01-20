package com.microservicio.trazabilidad.domain.usecase;

import com.microservicio.trazabilidad.domain.api.ITraceabilityServicePort;
import com.microservicio.trazabilidad.domain.constants.OrderConstants;
import com.microservicio.trazabilidad.domain.model.Traceability;
import com.microservicio.trazabilidad.domain.spi.ITraceabilityPersistencePort;

import java.util.List;
import java.util.stream.Collectors;

public class TraceabilityUseCase implements ITraceabilityServicePort {


    private final ITraceabilityPersistencePort traceabilityPersistencePort;

    public TraceabilityUseCase(ITraceabilityPersistencePort traceabilityPersistencePort) {
        this.traceabilityPersistencePort = traceabilityPersistencePort;
    }

    @Override
    public void saveTraceability(Traceability traceability) {
        traceabilityPersistencePort.save(traceability);
    }

    @Override
    public List<Traceability> getTraceabilityByClient(String clientId) {
        return traceabilityPersistencePort.findByClient(clientId);
    }

    @Override
    public List<Traceability> getTraceabilityByOrder(Long orderId) {
        return traceabilityPersistencePort.findByOrder(orderId);
    }

    @Override
    public List<Traceability> getTraceabilityByClientAndOrder(String clientId, Long orderId) {
        return traceabilityPersistencePort.findByClientAndOrder(clientId, orderId);
    }

    @Override
    public List<Traceability> getOrdersEfficiency(String restaurantId) {
        List<Traceability> allTraces = traceabilityPersistencePort.findByRestaurant(restaurantId);

        return allTraces.stream()
                .filter(trace ->
                        (trace.getPreviousStatus() == null && OrderConstants.AVAILABLE.toString().equals(trace.getNewStatus())) ||
                                OrderConstants.DELIVERED.toString().equals(trace.getNewStatus())
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Traceability> getEmployeesEfficiency(String restaurantId) {
        List<Traceability> allTraces = traceabilityPersistencePort.findByRestaurant(restaurantId);

        return allTraces.stream()
                .filter(trace ->
                        trace.getEmployeeId() != null &&
                                (OrderConstants.PREPARATION.toString().equals(trace.getNewStatus()) ||
                                        OrderConstants.DELIVERED.toString().equals(trace.getNewStatus()))
                )
                .collect(Collectors.toList());
    }
}