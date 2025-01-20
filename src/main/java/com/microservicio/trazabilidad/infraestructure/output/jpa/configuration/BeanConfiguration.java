package com.microservicio.trazabilidad.infraestructure.output.jpa.configuration;

import com.microservicio.trazabilidad.domain.api.ITraceabilityServicePort;
import com.microservicio.trazabilidad.domain.spi.ITraceabilityPersistencePort;
import com.microservicio.trazabilidad.domain.usecase.TraceabilityUseCase;
import com.microservicio.trazabilidad.infraestructure.output.mongodb.adapter.TraceabilityAdapter;
import com.microservicio.trazabilidad.infraestructure.output.mongodb.mapper.TraceabilityEntityMapper;
import com.microservicio.trazabilidad.infraestructure.output.mongodb.repository.ITraceabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final ITraceabilityRepository traceabilityRepository;

    @Bean
    public TraceabilityEntityMapper traceabilityEntityMapper() {
        return new TraceabilityEntityMapper();
    }

    @Bean
    public ITraceabilityPersistencePort traceabilityPersistencePort(TraceabilityEntityMapper traceabilityEntityMapper) {
        return new TraceabilityAdapter(traceabilityRepository, traceabilityEntityMapper);
    }

    @Bean
    public ITraceabilityServicePort traceabilityServicePort(ITraceabilityPersistencePort traceabilityPersistencePort) {
        return new TraceabilityUseCase(traceabilityPersistencePort);
    }
}
