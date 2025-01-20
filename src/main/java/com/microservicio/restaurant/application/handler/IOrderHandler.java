package com.microservicio.restaurant.application.handler;

import com.microservicio.restaurant.application.dto.CreateOrderRequest;
import com.microservicio.restaurant.application.dto.OrderResponse;
import org.springframework.data.domain.Page;

public interface IOrderHandler {

    OrderResponse createOrder(CreateOrderRequest createOrderRequest, Long clientId);

    boolean hasActiveOrders(Long clientId);

    Page<OrderResponse> findOrdersByStatus(String status, int page, int size);

    OrderResponse assignOrderToEmployee(Long orderId);

    void updateOrderStatusByEmployee(Long orderId, Long employeeId, Long newStatus);

    void deliverOrder(Long orderId, String securityPin);

    void cancelOrder(Long orderId, Long clientId);

}