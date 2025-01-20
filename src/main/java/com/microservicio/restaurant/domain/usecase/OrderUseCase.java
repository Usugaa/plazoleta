package com.microservicio.restaurant.domain.usecase;

import com.microservicio.restaurant.domain.api.IOrderServicePort;
import com.microservicio.restaurant.domain.constants.OrderConstants;
import com.microservicio.restaurant.domain.model.Order;
import com.microservicio.restaurant.domain.model.OrderDishes;
import com.microservicio.restaurant.domain.model.SmsMessage;
import com.microservicio.restaurant.domain.model.Traceability;
import com.microservicio.restaurant.domain.spi.IMessagePersistencePort;
import com.microservicio.restaurant.domain.spi.IOrderPersistencePort;
import com.microservicio.restaurant.domain.spi.ITraceabilityPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IMessagePersistencePort messagePersistencePort;
    private final ITraceabilityPersistencePort traceabilityPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IMessagePersistencePort messagePersistencePort,
                        ITraceabilityPersistencePort traceabilityPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.messagePersistencePort = messagePersistencePort;
        this.traceabilityPersistencePort = traceabilityPersistencePort;
    }

    @Override
    public Order createOrder(Long clientId, Long restaurantId, List<OrderDishes> orderDishes) {
        // Quitar la validación de clientId null ya que se manejará en el adapter

        // Crear nueva orden
        Order order = new Order(
                null,
                null,  // El clientId lo establecerá el adapter
                new Date(),
                OrderConstants.AVAILABLE.toString(),
                null,
                restaurantId,
                null
        );

        // Guardar la orden
        Order savedOrder = orderPersistencePort.saveOrder(order);
        if (savedOrder == null) {
            throw new IllegalArgumentException("No se pudo guardar la orden");
        }

        // Registrar trazabilidad de creación de orden
        Traceability traceability = new Traceability.TraceabilityBuilder()
                .orderId(savedOrder.getId())
                .clientId(savedOrder.getIdClient().toString())  // Usar el ID del cliente de la orden guardada
                .restaurantId(savedOrder.getIdRestaurant().toString())
                .status(null, OrderConstants.AVAILABLE.toString())
                .build();

        traceabilityPersistencePort.save(traceability);

        // Preparar los platos de la orden con el ID de la orden
        List<OrderDishes> orderDishesToSave = orderDishes.stream()
                .map(od -> new OrderDishes(
                        null,  // ID será generado por la BD
                        savedOrder.getId(),  // Asignar el ID de la orden guardada
                        od.getIdDish(),
                        od.getAmount()
                ))
                .collect(Collectors.toList());

        // Guardar los platos de la orden
        List<OrderDishes> savedOrderDishes = orderPersistencePort.saveOrderDishes(orderDishesToSave);
        if (savedOrderDishes == null || savedOrderDishes.isEmpty()) {
            throw new IllegalArgumentException("No se pudieron guardar los platos de la orden");
        }

        // Devolver la orden completa
        return savedOrder;
    }

    @Override
    public void cancelOrder(Long orderId, Long clientId) {
        // Obtener la orden actual para validar su estado
        Order order = orderPersistencePort.findOrderById(orderId);

        // Validar que la orden esté en estado AVAILABLE (pendiente)
        if (!OrderConstants.AVAILABLE.toString().equals(order.getStatus())) {
            throw new IllegalStateException("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }

        // Establecer nuevo estado CANCEL y actualizar
        orderPersistencePort.cancelOrder(orderId, clientId);

        // Registrar trazabilidad de la cancelación
        traceabilityPersistencePort.save(new Traceability.TraceabilityBuilder()
                .orderId(orderId)
                .clientId(clientId.toString())
                .status(OrderConstants.AVAILABLE.toString(), OrderConstants.CANCEL.toString())
                .build());
    }

    @Override
    public boolean hasActiveOrders(Long clientId) {
        // Verificar si el cliente tiene órdenes activas
        return orderPersistencePort.existsActiveOrdersByClient(clientId);
    }

    @Override
    public Page<Order> findOrdersByStatus(String status, Pageable pageable) {
        return orderPersistencePort.findOrdersByStatus(status, pageable);
    }

    @Override
    public Order assignOrderToEmployee(Long orderId) {
        // Obtener la orden actual para tener el estado anterior
        Order currentOrder = orderPersistencePort.findOrderById(orderId);
        String previousStatus = currentOrder.getStatus();

        // Delegar la lógica al puerto de persistencia
        Order updatedOrder = orderPersistencePort.assignOrderToEmployee(orderId);

        // Registrar trazabilidad de asignación
        Traceability traceability = new Traceability.TraceabilityBuilder()
                .orderId(orderId)
                .clientId(updatedOrder.getIdClient().toString())
                .status(previousStatus, OrderConstants.PREPARATION.toString())
                .employee(updatedOrder.getIdEmpleoyee(), null)
                .build();

        traceabilityPersistencePort.save(traceability);

        return updatedOrder;
    }

    @Override
    public void updateOrderStatusByEmployee(Long orderId, Long employeeId, Long newStatus) {
        // Obtener la orden completa
        Order order = orderPersistencePort.findOrderById(orderId);

        // Guardar estado anterior
        String previousStatus = order.getStatus();

        // Establecer nuevo estado
        order.setStatus(newStatus.toString());

        // Actualizar estado
        Order updatedOrder = orderPersistencePort.updateOrderStatus(order);

        // Registrar trazabilidad del cambio de estado
        Traceability traceability = new Traceability.TraceabilityBuilder()
                .orderId(orderId)
                .clientId(order.getIdClient().toString())
                .status(previousStatus, newStatus.toString())
                .employee(employeeId, null)
                .build();

        traceabilityPersistencePort.save(traceability);

        // Si el estado es READY, enviar notificación SMS
        if (OrderConstants.READY.toString().equals(updatedOrder.getStatus())) {
            SmsMessage message = new SmsMessage(
                    updatedOrder.getId(),
                    updatedOrder.getSecurityPin(),
                    null
            );
            messagePersistencePort.sendOrderReadyMessage(message);
        }
    }

    @Override
    public void deliverOrder(Long orderId, String securityPin) {
        // Obtener la orden para guardar el estado anterior
        Order order = orderPersistencePort.findOrderById(orderId);
        String previousStatus = order.getStatus();

        // Entregar orden y actualizar estado
        orderPersistencePort.deliverOrder(orderId, securityPin);

        // Registrar trazabilidad de entrega
        Traceability traceability = new Traceability.TraceabilityBuilder()
                .orderId(orderId)
                .clientId(order.getIdClient().toString())
                .status(previousStatus, OrderConstants.DELIVERED.toString())
                .build();

        traceabilityPersistencePort.save(traceability);

        // Enviar notificación de entrega
        try {
            SmsMessage message = new SmsMessage(orderId, null, null);
            messagePersistencePort.sendOrderDeliveredMessage(message);
        } catch (Exception e) {
            log.error("Error al enviar notificación de entrega: {}", e.getMessage());
            // Continuamos con la ejecución ya que la orden fue entregada exitosamente
        }
    }
}