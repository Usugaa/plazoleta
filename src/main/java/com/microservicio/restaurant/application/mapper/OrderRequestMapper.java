package com.microservicio.restaurant.application.mapper;

import com.microservicio.restaurant.application.dto.CreateOrderRequest;
import com.microservicio.restaurant.application.dto.OrderResponse;
import com.microservicio.restaurant.domain.constants.OrderConstants;
import com.microservicio.restaurant.domain.model.Order;

import java.util.Date;

public class OrderRequestMapper {

    public Order toOrder(CreateOrderRequest request, Long clientId) {
        return new Order(
                null,
                clientId,
                new Date(),
                OrderConstants.AVAILABLE.toString(),
                null,
                request.getRestaurantId(),
                null
        );
    }

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setIdClient(order.getIdClient());
        response.setDate(order.getDate());
        response.setStatus(order.getStatus());
        response.setIdEmployee(order.getIdEmpleoyee());
        response.setIdRestaurant(order.getIdRestaurant());
        response.setSecurityPin(order.getSecurityPin());  // Mapeamos el nuevo campo
        return response;
    }
}