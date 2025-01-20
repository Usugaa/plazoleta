package com.microservicio.restaurant.infraestructure.output.jpa.adapter;

import com.microservicio.restaurant.application.dto.EmployeeEfficiencyResponse;
import com.microservicio.restaurant.application.dto.OrderEfficiencyResponse;
import com.microservicio.restaurant.application.dto.TraceabilityRequest;
import com.microservicio.restaurant.application.dto.TraceabilityResponse;
import com.microservicio.restaurant.domain.constants.OrderConstants;
import com.microservicio.restaurant.domain.model.Traceability;
import com.microservicio.restaurant.domain.spi.ITraceabilityPersistencePort;
import com.microservicio.restaurant.infraestructure.input.client.TraceabilityFeignClient;
import com.microservicio.restaurant.infraestructure.output.jpa.exceptions.RestaurantPersistenceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TraceabilityAdapter implements ITraceabilityPersistencePort {

    private final TraceabilityFeignClient traceabilityFeignClient;

    @Override
    public void save(Traceability traceability) {
        try {
            log.info("Guardando trazabilidad para la orden: {}", traceability.getOrderId());
            log.info("Datos completos de trazabilidad: orderId={}, clientId={}, clientEmail={}, previousStatus={}, newStatus={}, employeeId={}, employeeEmail={}, restaurantId={}",
                    traceability.getOrderId(),
                    traceability.getClientId(),
                    traceability.getClientEmail(),
                    traceability.getPreviousStatus(),
                    traceability.getNewStatus(),
                    traceability.getEmployeeId(),
                    traceability.getEmployeeEmail(),
                    traceability.getRestaurantId());

            TraceabilityRequest request = new TraceabilityRequest(
                    traceability.getOrderId(),
                    traceability.getClientId(),
                    traceability.getClientEmail(),
                    traceability.getPreviousStatus(),
                    traceability.getNewStatus(),
                    traceability.getEmployeeId(),
                    traceability.getEmployeeEmail(),
                    traceability.getRestaurantId()
            );
            log.info("Request de trazabilidad a enviar: {}", request.toString());  // Añadir toString()

            ResponseEntity<Void> response = traceabilityFeignClient.saveTraceability(request);
            log.info("Respuesta del servidor: {}", response.getStatusCode());
        } catch (FeignException e) {
            log.error("Error detallado de Feign: {}", e.contentUTF8());  // Esto mostrará el mensaje de error del servidor
            throw new RestaurantPersistenceException("Error en la comunicación con el servicio de trazabilidad: " + e.getMessage());
        }
    }

    @Override
    public List<Traceability> findByClient(String clientId) {
        try {
            log.info("Buscando trazabilidad para el cliente: {}", clientId);
            List<TraceabilityResponse> responses = traceabilityFeignClient.getTraceabilityByClient(clientId).getBody();
            return responses.stream()
                    .map(response -> new Traceability.TraceabilityBuilder()
                            .orderId(response.getOrderId())
                            .clientId(response.getClientId())
                            .clientEmail(response.getClientEmail())
                            .status(response.getPreviousStatus(), response.getNewStatus())
                            .employee(response.getEmployeeId(), response.getEmployeeEmail())
                            .restaurantId(response.getRestaurantId())
                            .build())
                    .collect(Collectors.toList());
        } catch (FeignException e) {
            log.error("Error al buscar trazabilidad por cliente: {}", e.getMessage());
            throw new RestaurantPersistenceException("Error al obtener trazabilidad del cliente: " + e.getMessage());
        }
    }

    @Override
    public List<Traceability> findByOrder(Long orderId) {
        try {
            log.info("Buscando trazabilidad para la orden: {}", orderId);
            List<TraceabilityResponse> responses = traceabilityFeignClient.getTraceabilityByOrder(orderId).getBody();
            return responses.stream()
                    .map(response -> new Traceability.TraceabilityBuilder()
                            .orderId(response.getOrderId())
                            .clientId(response.getClientId())
                            .clientEmail(response.getClientEmail())
                            .status(response.getPreviousStatus(), response.getNewStatus())
                            .employee(response.getEmployeeId(), response.getEmployeeEmail())
                            .restaurantId(response.getRestaurantId())
                            .build())
                    .collect(Collectors.toList());
        } catch (FeignException e) {
            log.error("Error al buscar trazabilidad por orden: {}", e.getMessage());
            throw new RestaurantPersistenceException("Error al obtener trazabilidad de la orden: " + e.getMessage());
        }
    }

    @Override
    public List<Traceability> findByClientAndOrder(String clientId, Long orderId) {
        try {
            log.info("Buscando trazabilidad para el cliente: {} y orden: {}", clientId, orderId);
            List<TraceabilityResponse> responses = traceabilityFeignClient.getTraceabilityByClientAndOrder(clientId, orderId).getBody();
            return responses.stream()
                    .map(response -> new Traceability.TraceabilityBuilder()
                            .orderId(response.getOrderId())
                            .clientId(response.getClientId())
                            .clientEmail(response.getClientEmail())
                            .status(response.getPreviousStatus(), response.getNewStatus())
                            .employee(response.getEmployeeId(), response.getEmployeeEmail())
                            .restaurantId(response.getRestaurantId())
                            .build())
                    .collect(Collectors.toList());
        } catch (FeignException e) {
            log.error("Error al buscar trazabilidad por cliente y orden: {}", e.getMessage());
            throw new RestaurantPersistenceException("Error al obtener trazabilidad: " + e.getMessage());
        }
    }

    @Override
    public List<Traceability> findByRestaurantId(String restaurantId) {
        try {
            log.info("Buscando eficiencia para el restaurante: {}", restaurantId);
            List<OrderEfficiencyResponse> orderEfficiency = traceabilityFeignClient.getOrdersEfficiency(restaurantId).getBody();
            List<EmployeeEfficiencyResponse> employeeEfficiency = traceabilityFeignClient.getEmployeesEfficiency(restaurantId).getBody();

            // Aquí necesitarás mapear las respuestas al modelo Traceability
            // Dependiendo de cómo quieras mostrar la información
            return mapEfficiencyToTraceability(orderEfficiency, employeeEfficiency);
        } catch (FeignException e) {
            log.error("Error al buscar eficiencia por restaurante: {}", e.getMessage());
            throw new RestaurantPersistenceException("Error al obtener eficiencia del restaurante: " + e.getMessage());
        }
    }

private List<Traceability> mapEfficiencyToTraceability(
            List<OrderEfficiencyResponse> orderEfficiency,
            List<EmployeeEfficiencyResponse> employeeEfficiency) {
        List<Traceability> traceabilities = new ArrayList<>();

        // Mapear eficiencia de órdenes
        if (orderEfficiency != null) {
            orderEfficiency.forEach(order -> {
                // Para el estado inicial del pedido
                traceabilities.add(new Traceability.TraceabilityBuilder()
                        .orderId(order.getOrderId())
                        .status(null, OrderConstants.AVAILABLE.toString()) // Estado inicial
                        .build());

                // Para el estado final del pedido
                traceabilities.add(new Traceability.TraceabilityBuilder()
                        .orderId(order.getOrderId())
                        .status(OrderConstants.PREPARATION.toString(), OrderConstants.DELIVERED.toString()) // Estado final
                        .build());
            });
        }

        // Mapear eficiencia de empleados
    if (employeeEfficiency != null) {
        employeeEfficiency.forEach(employee -> traceabilities.add(
                new Traceability.TraceabilityBuilder()
                        .employee(employee.getEmployeeId(), employee.getEmployeeEmail())
                        .status(OrderConstants.PREPARATION.toString(), OrderConstants.DELIVERED.toString())
                        .build()
        ));
    }

    return traceabilities;
    }
}
