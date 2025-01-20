package com.microservicio.trazabilidad.infraestructure.output.mongodb.adapter;

import com.microservicio.trazabilidad.domain.model.Traceability;
import com.microservicio.trazabilidad.domain.spi.ITraceabilityPersistencePort;
import com.microservicio.trazabilidad.infraestructure.output.mongodb.mapper.TraceabilityEntityMapper;
import com.microservicio.trazabilidad.infraestructure.output.mongodb.repository.ITraceabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TraceabilityAdapter implements ITraceabilityPersistencePort {

    private final ITraceabilityRepository traceabilityRepository;
    private final TraceabilityEntityMapper traceabilityEntityMapper;

    @Override
    public void save(Traceability traceability) {
        traceabilityRepository.save(traceabilityEntityMapper.toEntity(traceability));
    }

    @Override
    public List<Traceability> findByClient(String clientId) {
        return traceabilityEntityMapper.toTraceabilityList(
                traceabilityRepository.findByClientId(clientId)
        );
    }

    @Override
    public List<Traceability> findByOrder(Long orderId) {
        return traceabilityEntityMapper.toTraceabilityList(
                traceabilityRepository.findByOrderId(orderId)
        );
    }

    @Override
    public List<Traceability> findByClientAndOrder(String clientId, Long orderId) {
        return traceabilityEntityMapper.toTraceabilityList(
                traceabilityRepository.findByClientIdAndOrderId(clientId, orderId)
        );
    }

    @Override
    public List<Traceability> findByRestaurant(String restaurantId) {
        return traceabilityEntityMapper.toTraceabilityList(
                traceabilityRepository.findByRestaurantId(restaurantId)
        );
    }
}
