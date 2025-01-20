package com.microservicio.trazabilidad.domain.spi;

import com.microservicio.trazabilidad.domain.model.Traceability;

import java.util.List;


public interface ITraceabilityPersistencePort {

    void save(Traceability traceability);
    List<Traceability> findByClient(String clientId);
    List<Traceability> findByOrder(Long orderId);
    List<Traceability> findByClientAndOrder(String clientId, Long orderId);
    List<Traceability> findByRestaurant(String restaurantId);

}
