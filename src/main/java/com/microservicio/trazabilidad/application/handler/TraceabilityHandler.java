package com.microservicio.trazabilidad.application.handler;

import com.microservicio.trazabilidad.application.dto.EmployeeEfficiencyResponse;
import com.microservicio.trazabilidad.application.dto.OrderEfficiencyResponse;
import com.microservicio.trazabilidad.application.dto.TraceabilityRequest;
import com.microservicio.trazabilidad.application.dto.TraceabilityResponse;
import com.microservicio.trazabilidad.application.mapper.EfficiencyMapper;
import com.microservicio.trazabilidad.application.mapper.TraceabilityMapper;
import com.microservicio.trazabilidad.domain.api.ITraceabilityServicePort;
import com.microservicio.trazabilidad.domain.model.Traceability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TraceabilityHandler implements ITraceabilityHandler{

    private final ITraceabilityServicePort traceabilityServicePort;
    private final TraceabilityMapper traceabilityMapper;
    private final EfficiencyMapper efficiencyMapper;

    @Override
    public void saveTraceability(TraceabilityRequest traceabilityRequest) {
        Traceability traceability = traceabilityMapper.toTraceability(traceabilityRequest);
        traceabilityServicePort.saveTraceability(traceability);
    }

    @Override
    public List<TraceabilityResponse> getTraceabilityByClient(String clientId) {
        List<Traceability> traceabilityList = traceabilityServicePort.getTraceabilityByClient(clientId);
        return traceabilityMapper.toResponseList(traceabilityList);
    }

    @Override
    public List<TraceabilityResponse> getTraceabilityByOrder(Long orderId) {
        List<Traceability> traceabilityList = traceabilityServicePort.getTraceabilityByOrder(orderId);
        return traceabilityMapper.toResponseList(traceabilityList);
    }

    @Override
    public List<TraceabilityResponse> getTraceabilityByClientAndOrder(String clientId, Long orderId) {
        List<Traceability> traceabilityList = traceabilityServicePort.getTraceabilityByClientAndOrder(clientId, orderId);
        return traceabilityMapper.toResponseList(traceabilityList);
    }

    @Override
    public List<OrderEfficiencyResponse> getOrdersEfficiency(String restaurantId) {
        return efficiencyMapper.toOrderEfficiencyResponseList(
                traceabilityServicePort.getOrdersEfficiency(restaurantId)
        );
    }

    @Override
    public List<EmployeeEfficiencyResponse> getEmployeesEfficiency(String restaurantId) {
        return efficiencyMapper.toEmployeeEfficiencyResponseList(
                traceabilityServicePort.getEmployeesEfficiency(restaurantId)
        );
    }
}
