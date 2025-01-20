package com.microservicio.trazabilidad.infraestructure.output.mongodb.repository;

import com.microservicio.trazabilidad.infraestructure.output.mongodb.entity.TraceabilityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITraceabilityRepository extends MongoRepository<TraceabilityEntity, Long> {

    List<TraceabilityEntity> findByClientId(String clientId);
    List<TraceabilityEntity> findByOrderId(Long orderId);
    List<TraceabilityEntity> findByClientIdAndOrderId(String clientId, Long orderId);
    List<TraceabilityEntity> findByRestaurantId(String restaurantId);
}
