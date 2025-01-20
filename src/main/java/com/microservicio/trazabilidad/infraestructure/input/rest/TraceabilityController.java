package com.microservicio.trazabilidad.infraestructure.input.rest;

import com.microservicio.trazabilidad.application.dto.EmployeeEfficiencyResponse;
import com.microservicio.trazabilidad.application.dto.OrderEfficiencyResponse;
import com.microservicio.trazabilidad.application.dto.TraceabilityRequest;
import com.microservicio.trazabilidad.application.dto.TraceabilityResponse;
import com.microservicio.trazabilidad.application.handler.ITraceabilityHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/traceability")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Traceability", description = "Endpoints for tracking order and employee traceability")
public class TraceabilityController {

    private final ITraceabilityHandler traceabilityHandler;

    @PostMapping("/saveTraceability")
    @Operation(summary = "Save a new traceability record",
            description = "Creates a new traceability entry for an order")
    @ApiResponse(responseCode = "201", description = "Traceability saved successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<Void> saveTraceability(
            @Parameter(description = "Traceability details", required = true)
            @RequestBody TraceabilityRequest request) {
        try {
            traceabilityHandler.saveTraceability(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get traceability by client ID",
            description = "Retrieves all traceability records for a specific client")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved traceability records",
            content = @Content(schema = @Schema(implementation = TraceabilityResponse.class)))
    public ResponseEntity<List<TraceabilityResponse>> getTraceabilityByClient(
            @Parameter(description = "Client ID", required = true)
            @PathVariable String clientId) {
        return ResponseEntity.ok(traceabilityHandler.getTraceabilityByClient(clientId));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get traceability by order ID",
            description = "Retrieves all traceability records for a specific order")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved traceability records",
            content = @Content(schema = @Schema(implementation = TraceabilityResponse.class)))
    public ResponseEntity<List<TraceabilityResponse>> getTraceabilityByOrder(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId) {
        return ResponseEntity.ok(traceabilityHandler.getTraceabilityByOrder(orderId));
    }

    @GetMapping("/client/{clientId}/order/{orderId}")
    @Operation(summary = "Get traceability by client and order ID",
            description = "Retrieves traceability records for a specific client and order")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved traceability records",
            content = @Content(schema = @Schema(implementation = TraceabilityResponse.class)))
    public ResponseEntity<List<TraceabilityResponse>> getTraceabilityByClientAndOrder(
            @Parameter(description = "Client ID", required = true)
            @PathVariable String clientId,
            @Parameter(description = "Order ID", required = true)
            @PathVariable Long orderId) {
        return ResponseEntity.ok(traceabilityHandler.getTraceabilityByClientAndOrder(clientId, orderId));
    }

    @GetMapping("/efficiency/orders/{restaurantId}")
    @Operation(summary = "Get order efficiency",
            description = "Retrieves efficiency metrics for orders in a specific restaurant")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved order efficiency",
            content = @Content(schema = @Schema(implementation = OrderEfficiencyResponse.class)))
    public ResponseEntity<List<OrderEfficiencyResponse>> getOrdersEfficiency(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable String restaurantId) {
        return ResponseEntity.ok(traceabilityHandler.getOrdersEfficiency(restaurantId));
    }

    @GetMapping("/efficiency/employees/{restaurantId}")
    @Operation(summary = "Get employee efficiency",
            description = "Retrieves efficiency metrics for employees in a specific restaurant")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved employee efficiency",
            content = @Content(schema = @Schema(implementation = EmployeeEfficiencyResponse.class)))
    public ResponseEntity<List<EmployeeEfficiencyResponse>> getEmployeesEfficiency(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable String restaurantId) {
        return ResponseEntity.ok(traceabilityHandler.getEmployeesEfficiency(restaurantId));
    }
}