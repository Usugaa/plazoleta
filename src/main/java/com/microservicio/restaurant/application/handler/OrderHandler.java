package com.microservicio.restaurant.application.handler;

import com.microservicio.restaurant.application.dto.CreateOrderRequest;
import com.microservicio.restaurant.application.dto.OrderDishResponse;
import com.microservicio.restaurant.application.dto.OrderResponse;
import com.microservicio.restaurant.application.mapper.OrderDishesRequestMapper;
import com.microservicio.restaurant.application.mapper.OrderRequestMapper;
import com.microservicio.restaurant.domain.api.IOrderServicePort;
import com.microservicio.restaurant.domain.model.Order;
import com.microservicio.restaurant.domain.model.OrderDishes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderService;
    private final OrderRequestMapper orderRequestMapper;
    private final OrderDishesRequestMapper orderDishesRequestMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest, Long clientId) {
        try {
            // Convertir los platos del request a OrderDishes de dominio
            List<OrderDishes> orderDishes = orderDishesRequestMapper.toOrderDishes(
                    createOrderRequest.getDishes(),
                    null  // El ID de orden se asignará en el servicio
            );

            // Crear la orden
            Order savedOrder = orderService.createOrder(
                    clientId,
                    createOrderRequest.getRestaurantId(),
                    orderDishes
            );

            // Si la orden es nula (por ejemplo, debido a órdenes activas), lanzar una excepción
            if (savedOrder == null) {
                throw new IllegalArgumentException("No se puede crear la orden. El cliente ya tiene órdenes activas.");
            }

            // Convertir la orden a OrderResponse
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(savedOrder.getId());
            orderResponse.setIdClient(savedOrder.getIdClient());
            orderResponse.setDate(savedOrder.getDate());
            orderResponse.setStatus(savedOrder.getStatus());
            orderResponse.setIdEmployee(savedOrder.getIdEmpleoyee());
            orderResponse.setIdRestaurant(savedOrder.getIdRestaurant());

            // Convertir los platos a OrderDishResponse
            if (savedOrder.getId() != null) {
                List<OrderDishResponse> dishResponses = orderDishesRequestMapper.toOrderDishesResponse(orderDishes);
                orderResponse.setDishes(dishResponses);
            }

            return orderResponse;

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pudo crear la orden: " + e.getMessage()
            );
        }
    }

    @Override
    public void cancelOrder(Long orderId, Long clientId) {
        try {
            orderService.cancelOrder(orderId, clientId);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cancelar el pedido: " + e.getMessage()
            );
        }
    }

    @Override
    public boolean hasActiveOrders(Long clientId) {
        return orderService.hasActiveOrders(clientId);
    }

    @Override
    public Page<OrderResponse> findOrdersByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.findOrdersByStatus(status, pageable);

        // Convertir a respuestas
        return orders.map(order -> {
            OrderResponse response = new OrderResponse();
            response.setId(order.getId());
            response.setIdClient(order.getIdClient());
            response.setDate(order.getDate());
            response.setStatus(order.getStatus());
            response.setIdEmployee(order.getIdEmpleoyee());
            response.setIdRestaurant(order.getIdRestaurant());
            return response;
        });
    }

    @Override
    public OrderResponse assignOrderToEmployee(Long orderId) {
        // Delegar toda la lógica de autenticación y asignación al servicio
        Order assignedOrder = orderService.assignOrderToEmployee(orderId);

        // Convertir a respuesta
        OrderResponse response = new OrderResponse();
        response.setId(assignedOrder.getId());
        response.setIdClient(assignedOrder.getIdClient());
        response.setDate(assignedOrder.getDate());
        response.setStatus(assignedOrder.getStatus());
        response.setIdEmployee(assignedOrder.getIdEmpleoyee());
        response.setIdRestaurant(assignedOrder.getIdRestaurant());

        return response;
    }

    @Override
    public void updateOrderStatusByEmployee(Long orderId, Long employeeId, Long newStatus) {
        orderService.updateOrderStatusByEmployee(orderId, employeeId, newStatus);
    }

    @Override
    public void deliverOrder(Long orderId, String securityPin) {
        try {
            orderService.deliverOrder(orderId, securityPin);
        } catch (Exception e){
            throw new IllegalArgumentException("Error al entregar el pedido: " + e.getMessage());
        }
    }
}