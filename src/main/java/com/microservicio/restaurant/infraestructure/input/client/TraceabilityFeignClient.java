package com.microservicio.restaurant.infraestructure.input.client;

import com.microservicio.restaurant.application.dto.EmployeeEfficiencyResponse;
import com.microservicio.restaurant.application.dto.OrderEfficiencyResponse;
import com.microservicio.restaurant.application.dto.TraceabilityRequest;
import com.microservicio.restaurant.application.dto.TraceabilityResponse;
import com.microservicio.restaurant.infraestructure.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "trazabilidad", url = "http://localhost:8085", configuration = FeignConfiguration.class)
public interface TraceabilityFeignClient {

    @PostMapping("/traceability/saveTraceability")
    ResponseEntity<Void> saveTraceability(@RequestBody TraceabilityRequest request);

    @GetMapping("/traceability/client/{clientId}")
    ResponseEntity<List<TraceabilityResponse>> getTraceabilityByClient(@PathVariable String clientId);

    @GetMapping("/traceability/order/{orderId}")
    ResponseEntity<List<TraceabilityResponse>> getTraceabilityByOrder(@PathVariable Long orderId);

    @GetMapping("/traceability/client/{clientId}/order/{orderId}")
    ResponseEntity<List<TraceabilityResponse>> getTraceabilityByClientAndOrder(
            @PathVariable String clientId,
            @PathVariable Long orderId
    );

    @GetMapping("/traceability/efficiency/orders/{restaurantId}")
    ResponseEntity<List<OrderEfficiencyResponse>> getOrdersEfficiency(@PathVariable String restaurantId);

    @GetMapping("/traceability/efficiency/employees/{restaurantId}")
    ResponseEntity<List<EmployeeEfficiencyResponse>> getEmployeesEfficiency(@PathVariable String restaurantId);
}
