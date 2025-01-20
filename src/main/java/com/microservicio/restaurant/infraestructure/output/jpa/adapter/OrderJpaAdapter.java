package com.microservicio.restaurant.infraestructure.output.jpa.adapter;

import com.microservicio.restaurant.domain.constants.OrderConstants;
import com.microservicio.restaurant.domain.constants.RoleConstants;
import com.microservicio.restaurant.domain.model.Order;
import com.microservicio.restaurant.domain.model.OrderDishes;
import com.microservicio.restaurant.domain.spi.IOrderPersistencePort;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.DishEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.OrderDishesEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.OrderEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.mapper.OrderEntityMapper;
import com.microservicio.restaurant.infraestructure.output.jpa.repository.IDishRepository;
import com.microservicio.restaurant.infraestructure.output.jpa.repository.IOrderDishesRepository;
import com.microservicio.restaurant.infraestructure.output.jpa.repository.IOrderRepository;
import com.microservicio.restaurant.infraestructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderDishesRepository orderDishesRepository;
    private final IDishRepository dishRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final JwtService jwtService;

    @Override
    public Order saveOrder(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        String token = (String) authentication.getCredentials();
        if (token == null) {
            return null;
        }

        try {
            Long clientId = jwtService.extractUserId(token);

            Order newOrder = new Order(
                    order.getId(),
                    clientId,
                    new Date(),
                    OrderConstants.AVAILABLE.toString(),
                    order.getIdEmpleoyee(),
                    order.getIdRestaurant(),
                    null
            );

            OrderEntity orderEntity = orderEntityMapper.toEntity(newOrder);
            OrderEntity savedEntity = orderRepository.save(orderEntity);
            return orderEntityMapper.toModel(savedEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void cancelOrder(Long orderId, Long clientId) {

        // Verificar autenticación
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null) {
            throw new IllegalArgumentException("No hay autenticación");
        }

        String token = (String) authentication.getCredentials();
        Long userRole = jwtService.extractRole(token);

        // Verificar que sea un cliente
        if (!RoleConstants.CLIENT.equals(userRole)) {
            log.warn("Usuario no autorizado. Role requerido: {}, Role actual: {}",
                    RoleConstants.CLIENT, userRole);
            throw new IllegalArgumentException("Solo los clientes pueden cancelar pedidos");
        }

        // Buscar la orden
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        // Verificar que la orden pertenece al cliente
        if (!clientId.equals(orderEntity.getIdClient())) {
            log.warn("El cliente {} no es el propietario de la orden {}", clientId, orderId);
            throw new IllegalArgumentException("No tienes permiso para cancelar esta orden");
        }

        // Verificar que la orden esté en estado AVAILABLE
        if (!OrderConstants.AVAILABLE.toString().equals(orderEntity.getStatus())) {
            log.warn("La orden {} no está en estado AVAILABLE. Estado actual: {}",
                    orderId, orderEntity.getStatus());
            throw new IllegalStateException("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }

        // Actualizar el estado a CANCEL
        orderEntity.setStatus(OrderConstants.CANCEL.toString());

        // Guardar los cambios
        orderRepository.save(orderEntity);
        log.info("Orden {} cancelada exitosamente por el cliente {}", orderId, clientId);
    }

    @Override
    public boolean existsActiveOrdersByClient(Long clientId) {
        List<String> activeStatuses = Arrays.asList(
                OrderConstants.AVAILABLE.toString(),   // Pendiente
                OrderConstants.PREPARATION.toString(), // En preparación
                OrderConstants.READY.toString()        // Listo
        );

        // Buscar órdenes con estos estados para el cliente específico
        List<OrderEntity> activeOrders = orderRepository.findByIdClientAndStatusIn(clientId, activeStatuses);

        return !activeOrders.isEmpty();
    }

    @Override
    public List<Order> findOrdersByClientAndStatuses(Long clientId, List<String> statuses) {
        List<OrderEntity> orders = orderRepository.findByIdClientAndStatusIn(clientId, statuses);
        return orders.stream()
                .map(orderEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Order> findOrdersByStatus(String status, Pageable pageable) {
        // Buscar órdenes por estado con paginación
        Page<OrderEntity> orderEntities = orderRepository.findByStatus(status, pageable);

        // Convertir las entidades a modelos de dominio
        return orderEntities.map(orderEntityMapper::toModel);
    }

    @Override
    public Order findOrderById(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
        return orderEntityMapper.toModel(orderEntity);
    }

    @Override
    public Order assignOrderToEmployee(Long orderId) {
        // Buscar la orden
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("No hay autenticación");
        }

        String token = (String) authentication.getCredentials();
        if (token == null) {
            throw new IllegalArgumentException("Token no encontrado");
        }

        // Extraer el ID del empleado del token
        Long employeeId = jwtService.extractUserId(token);

        // Buscar la orden
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        // Verificar que la orden no tenga ya un empleado asignado
        if (orderEntity.getIdEmployee() != null) {
            throw new IllegalArgumentException("La orden ya tiene un empleado asignado");
        }

        // Establecer el empleado y cambiar el estado
        orderEntity.setIdEmployee(employeeId);
        orderEntity.setStatus(OrderConstants.PREPARATION.toString());

        // Guardar la orden actualizada
        OrderEntity savedEntity = orderRepository.save(orderEntity);

        // Convertir y devolver el modelo de dominio
        return orderEntityMapper.toModel(savedEntity);
    }

    @Override
    public boolean isEmployeeAssignedToOrder(Long orderId, Long employeeId) {
        // Verificar autenticación y rol
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null) {
            return false;
        }

        String token = (String) authentication.getCredentials();
        String role = String.valueOf(jwtService.extractRole(token));

        // Verificar que sea un empleado
        if (!"EMPLEADO".equals(role)) {
            return false;
        }

        return orderRepository.findById(orderId)
                .map(order -> Objects.equals(employeeId, order.getIdEmployee()))
                .orElse(false);
    }

    @Override
    public Order updateOrderStatus(Order order) {
        log.info("Iniciando actualización de estado para la orden con ID: {}", order.getId());

        // Obtener el token del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null) {
            log.error("No se encontró autenticación o token en el contexto de seguridad");
            throw new IllegalArgumentException("No hay autenticación");
        }

        String token = (String) authentication.getCredentials();
        log.debug("Token obtenido del contexto de seguridad: {}", token);

        // Extraer ID de usuario y rol desde el token
        Long employeeId;
        Long userRole;
        try {
            employeeId = jwtService.extractUserId(token);
            userRole = jwtService.extractRole(token);
            log.debug("ID del empleado extraído: {}, Rol extraído: {}", employeeId, userRole);
        } catch (Exception e) {
            log.error("Error al extraer información del token: {}", e.getMessage());
            throw new IllegalArgumentException("Error al procesar el token de autenticación");
        }

        // Validar rol de empleado
        if (!RoleConstants.EMPLOYEE.equals(userRole)) {
            log.warn("El usuario con ID {} no tiene el rol de empleado. Rol actual: {}", employeeId, userRole);
            throw new IllegalArgumentException("Solo los empleados pueden actualizar el estado de la orden");
        }

        // Buscar la orden en la base de datos
        OrderEntity orderEntity = orderRepository.findById(order.getId())
                .orElseThrow(() -> {
                    log.error("No se encontró la orden con ID: {}", order.getId());
                    return new IllegalArgumentException("Orden no encontrada");
                });

        log.info("Orden encontrada con ID: {}, Estado actual: {}, ID del empleado asignado: {}",
                orderEntity.getId(), orderEntity.getStatus(), orderEntity.getIdEmployee());

        // Verificar estado actual y empleado asignado
        if (!OrderConstants.PREPARATION.toString().equals(orderEntity.getStatus())) {
            log.warn("La orden con ID {} no está en estado PREPARATION. Estado actual: {}",
                    orderEntity.getId(), orderEntity.getStatus());
            throw new IllegalArgumentException("La orden debe estar en PREPARATION para cambiarla");
        }

        if (!employeeId.equals(orderEntity.getIdEmployee())) {
            log.warn("El empleado con ID {} no está asignado a la orden con ID {}. Empleado asignado: {}",
                    employeeId, orderEntity.getId(), orderEntity.getIdEmployee());
            throw new IllegalArgumentException("Solo el empleado asignado puede actualizar el estado");
        }

        // Actualizar el estado
        log.info("Actualizando el estado de la orden con ID {} a {}", orderEntity.getId(), order.getStatus());
        orderEntity.setStatus(order.getStatus());

        // Generar PIN de seguridad si el estado es READY
        if (OrderConstants.READY.toString().equals(order.getStatus())) {
            String securityPin = generateSecurityPin();
            orderEntity.setSecurityPin(securityPin);
            log.info("Generado PIN de seguridad para la orden con ID {}: {}", orderEntity.getId(), securityPin);
        }

        // Guardar los cambios
        OrderEntity savedEntity = orderRepository.save(orderEntity);
        log.info("Orden con ID {} actualizada correctamente. Nuevo estado: {}",
                savedEntity.getId(), savedEntity.getStatus());

        // Retornar el modelo actualizado
        return orderEntityMapper.toModel(savedEntity);
    }

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private String generateSecurityPin() {
        String pin = String.format("%06d", SECURE_RANDOM.nextInt(1000000));
        log.debug("PIN de seguridad generado: {}", pin);
        return pin;
    }

    @Override
    public Order deliverOrder(Long orderId, String securityPin) {
        log.info("Iniciando proceso de entrega para la orden: {}", orderId);

        // Verificar autenticación
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null) {
            log.error("No se encontró autenticación válida");
            throw new IllegalArgumentException("No hay autenticación");
        }

        String token = (String) authentication.getCredentials();

        // Extraer información del empleado
        Long employeeId = jwtService.extractUserId(token);
        Long userRole = jwtService.extractRole(token);
        log.debug("ID del empleado: {}, Rol: {}", employeeId, userRole);

        if (!RoleConstants.EMPLOYEE.equals(userRole)) {
            throw new IllegalArgumentException("Solo los empleados pueden entregar pedidos");
        }

        // Buscar y validar la orden
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + orderId));

        log.info("Estado actual de la orden {}: {}", orderId, orderEntity.getStatus());

        // Verificar estado READY (usando el valor numérico 4)
        if (!OrderConstants.READY.toString().equals(orderEntity.getStatus())) {
            log.error("Estado inválido para la orden {}. Estado actual: {}, Estado esperado: {}",
                    orderId, orderEntity.getStatus(), OrderConstants.READY);
            throw new IllegalArgumentException("La orden debe estar en estado READY para ser entregada");
        }

        // Verificar empleado asignado
        if (!Objects.equals(employeeId, orderEntity.getIdEmployee())) {
            log.error("Empleado no autorizado para la orden. Empleado asignado: {}, Empleado intentando entregar: {}",
                    orderEntity.getIdEmployee(), employeeId);
            throw new IllegalArgumentException("Solo el empleado asignado puede entregar la orden");
        }

        // Verificar PIN
        if (!securityPin.equals(orderEntity.getSecurityPin())) {
            log.error("PIN de seguridad inválido para la orden {}", orderId);
            throw new IllegalArgumentException("PIN de seguridad inválido");
        }

        // Actualizar estado a DELIVERED (5)
        orderEntity.setStatus(OrderConstants.DELIVERED.toString());
        log.info("Actualizando estado de la orden {} a DELIVERED", orderId);

        OrderEntity savedEntity = orderRepository.save(orderEntity);
        log.info("Orden {} entregada exitosamente", orderId);

        return orderEntityMapper.toModel(savedEntity);
    }

    @Override
    public List<OrderDishes> saveOrderDishes(List<OrderDishes> orderDishes) {
        if (orderDishes.isEmpty()) {
            return List.of();
        }

        // Verificar mismo restaurante
        Long restaurantId = null;
        for (OrderDishes orderDish : orderDishes) {
            var dishOptional = dishRepository.findById(orderDish.getIdDish());
            if (dishOptional.isEmpty()) {
                return List.of();
            }

            var dish = dishOptional.get();
            if (restaurantId == null) {
                restaurantId = dish.getRestaurant().getId();
            } else if (!restaurantId.equals(dish.getRestaurant().getId())) {
                return List.of();
            }
        }

        List<OrderDishesEntity> entities = orderDishes.stream()
                .map(orderDish -> {
                    OrderDishesEntity entity = new OrderDishesEntity();

                    // Buscar la orden por ID
                    OrderEntity orderEntity = orderRepository.findById(orderDish.getIdOrder())
                            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

                    // Buscar el plato por ID
                    DishEntity dishEntity = dishRepository.findById(orderDish.getIdDish())
                            .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado"));

                    entity.setOrder(orderEntity);
                    entity.setDish(dishEntity);
                    entity.setAmount(orderDish.getAmount());

                    return entity;
                })
                .collect(Collectors.toList());

        List<OrderDishesEntity> savedEntities = orderDishesRepository.saveAll(entities);
        return savedEntities.stream()
                .map(orderEntityMapper::toOrderDishesModel)
                .toList();
    }
}