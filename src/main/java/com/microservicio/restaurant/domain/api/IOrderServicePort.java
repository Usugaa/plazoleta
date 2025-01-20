package com.microservicio.restaurant.domain.api;

import com.microservicio.restaurant.domain.model.Order;
import com.microservicio.restaurant.domain.model.OrderDishes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderServicePort {

    Order createOrder(Long clientId, Long restaurantId, List<OrderDishes> orderDishes);

    boolean hasActiveOrders(Long clientId);

    Page<Order> findOrdersByStatus(String status, Pageable pageable);

    Order assignOrderToEmployee(Long orderId);

    void updateOrderStatusByEmployee(Long orderId, Long employeeId, Long newStatus);

    void deliverOrder(Long orderId, String securityPin);

    void cancelOrder(Long orderId, Long clientId);

}
