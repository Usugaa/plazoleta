package com.microservicio.restaurant.infraestructure.input.rest;

import com.microservicio.restaurant.application.dto.CreateOrderRequest;
import com.microservicio.restaurant.application.dto.OrderResponse;
import com.microservicio.restaurant.application.handler.IOrderHandler;
import com.microservicio.restaurant.domain.constants.OrderConstants;
import com.microservicio.restaurant.domain.constants.RoleConstants;
import com.microservicio.restaurant.infraestructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Controller", description = "API para la gestión de órdenes de restaurante")
public class OrderController {

private final IOrderHandler orderHandler;
private final JwtService jwtService;

    @Operation(
            summary = "Crear una nueva orden",
            description = "Crea una nueva orden en el sistema"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Orden creada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
    )

    @PostMapping("/saveOrder")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        try {
            OrderResponse response = orderHandler.createOrder(createOrderRequest, null);

            if (response == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(
            summary = "Cancelar una orden",
            description = "Cancela una orden si está en estado pendiente"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Orden cancelada exitosamente"
    )
    @ApiResponse(
            responseCode = "400",
            description = "No se puede cancelar la orden porque no está en estado pendiente"
    )
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        try {
            // Obtener el token del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getCredentials() == null) {
                log.error("No se encontró autenticación o token en el contexto de seguridad");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = (String) authentication.getCredentials();
            Long clientId = jwtService.extractUserId(token);

            orderHandler.cancelOrder(orderId, clientId);
            log.info("Pedido {} cancelado exitosamente", orderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al cancelar pedido {}: {}", orderId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(
            summary = "Obtener órdenes por estado",
            description = "Recupera una lista paginada de órdenes filtradas por estado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de órdenes recuperada exitosamente",
            content = @Content(schema = @Schema(implementation = Page.class))
    )

    @GetMapping("/byStatus")
    public ResponseEntity<Page<OrderResponse>> getOrdersByStatus(
            @RequestParam(required = false, defaultValue = "1") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<OrderResponse> orders = orderHandler.findOrdersByStatus(status, page, size);
        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "Asignar orden a empleado",
            description = "Asigna una orden existente a un empleado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Orden asignada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))
    )

    @PutMapping("/assign/{orderId}")
    public ResponseEntity<OrderResponse> assignOrder(@PathVariable Long orderId) {
        OrderResponse assignedOrder = orderHandler.assignOrderToEmployee(orderId);
        return ResponseEntity.ok(assignedOrder);
    }

    @Operation(
            summary = "Marcar orden como lista",
            description = "Actualiza el estado de una orden a 'lista para entrega'",
            security = @SecurityRequirement(name = "JWT")
    )
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente")

            @ApiResponse(responseCode = "400", description = "Usuario no autorizado o datos inválidos")

    @PutMapping("/{orderId}/ready")
    public ResponseEntity<Void> markOrderAsReady(@PathVariable Long orderId) {
        // Obtener el token del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null) {
            log.error("No se encontró autenticación o token en el contexto de seguridad");
            throw new IllegalArgumentException("No hay autenticación");
        }

        String token = (String) authentication.getCredentials();
        Long employeeId = jwtService.extractUserId(token);

        // Verificar que el empleado tiene el rol adecuado
        Long userRole = jwtService.extractRole(token);
        if (!RoleConstants.EMPLOYEE.equals(userRole)) {
            log.warn("El usuario con ID {} no tiene el rol de empleado. Rol actual: {}", employeeId, userRole);
            throw new IllegalArgumentException("Solo los empleados pueden actualizar el estado de la orden");
        }

        orderHandler.updateOrderStatusByEmployee(orderId, employeeId, OrderConstants.READY);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Entregar orden",
            description = "Marca una orden como entregada usando un PIN de seguridad"
    )
    @ApiResponse(responseCode = "200", description = "Orden entregada exitosamente")
    @ApiResponse(responseCode = "400", description = "PIN inválido o error en la entrega")
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Void> deliverOrder(
            @PathVariable Long orderId,
            @RequestParam String securityPin) {
        try {
            orderHandler.deliverOrder(orderId, securityPin);
            log.info("Pedido {} entregado exitosamente", orderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al entregar pedido {}: {}", orderId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

