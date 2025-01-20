package com.microservicio.trazabilidad.usecase;

import com.microservicio.trazabilidad.domain.constants.OrderConstants;
import com.microservicio.trazabilidad.domain.model.Traceability;
import com.microservicio.trazabilidad.domain.spi.ITraceabilityPersistencePort;
import com.microservicio.trazabilidad.domain.usecase.TraceabilityUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraceabilityUseCaseTest {

    @Mock
    private ITraceabilityPersistencePort traceabilityPersistencePort;

    @InjectMocks
    private TraceabilityUseCase traceabilityUseCase;

    private Traceability traceability;

    @BeforeEach
    void setUp() {
        traceability = new Traceability.TraceabilityBuilder()
                .orderId(1L)
                .clientId("client1")
                .clientEmail("client@example.com")
                .status(null, OrderConstants.AVAILABLE.toString())
                .employee(1L, "employee@example.com")
                .restaurantId("restaurant1")
                .build();
    }

    @Test
    void saveTraceability_ShouldSaveSuccessfully() {
        // Given
        doNothing().when(traceabilityPersistencePort).save(any(Traceability.class));

        // When
        traceabilityUseCase.saveTraceability(traceability);

        // Then
        verify(traceabilityPersistencePort).save(traceability);
    }

    @Test
    void getTraceabilityByClient_ShouldReturnClientTraceability() {
        // Given
        String clientId = "client1";
        List<Traceability> expectedTraceabilities = Collections.singletonList(traceability);
        when(traceabilityPersistencePort.findByClient(clientId)).thenReturn(expectedTraceabilities);

        // When
        List<Traceability> result = traceabilityUseCase.getTraceabilityByClient(clientId);

        // Then
        assertNotNull(result);
        assertEquals(expectedTraceabilities, result);
        verify(traceabilityPersistencePort).findByClient(clientId);
    }

    @Test
    void getOrdersEfficiency_ShouldReturnFilteredTraceability() {
        // Given
        String restaurantId = "restaurant1";
        List<Traceability> allTraces = Arrays.asList(
                createTraceability(1L, null, OrderConstants.AVAILABLE.toString()),
                createTraceability(1L, OrderConstants.PREPARATION.toString(), OrderConstants.READY.toString()),
                createTraceability(1L, OrderConstants.READY.toString(), OrderConstants.DELIVERED.toString())
        );
        when(traceabilityPersistencePort.findByRestaurant(restaurantId)).thenReturn(allTraces);

        // When
        List<Traceability> result = traceabilityUseCase.getOrdersEfficiency(restaurantId);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(t ->
                t.getPreviousStatus() == null && OrderConstants.AVAILABLE.toString().equals(t.getNewStatus())));
        assertTrue(result.stream().anyMatch(t ->
                OrderConstants.DELIVERED.toString().equals(t.getNewStatus())));
    }

    @Test
    void getEmployeesEfficiency_ShouldReturnFilteredTraceability() {
        // Given
        String restaurantId = "restaurant1";
        List<Traceability> allTraces = Arrays.asList(
                createTraceability(1L, null, OrderConstants.AVAILABLE.toString()),
                createTraceability(1L, OrderConstants.AVAILABLE.toString(), OrderConstants.PREPARATION.toString()),
                createTraceability(1L, OrderConstants.PREPARATION.toString(), OrderConstants.DELIVERED.toString())
        );
        when(traceabilityPersistencePort.findByRestaurant(restaurantId)).thenReturn(allTraces);

        // When
        List<Traceability> result = traceabilityUseCase.getEmployeesEfficiency(restaurantId);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(t ->
                OrderConstants.PREPARATION.toString().equals(t.getNewStatus())));
        assertTrue(result.stream().anyMatch(t ->
                OrderConstants.DELIVERED.toString().equals(t.getNewStatus())));
    }

    private Traceability createTraceability(Long orderId, String previousStatus, String newStatus) {
        return new Traceability.TraceabilityBuilder()
                .orderId(orderId)
                .clientId("client1")
                .clientEmail("client@example.com")
                .status(previousStatus, newStatus)
                .employee(1L, "employee@example.com")
                .restaurantId("restaurant1")
                .build();
    }
}
