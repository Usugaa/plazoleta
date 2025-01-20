package com.microservicio.restaurant.domain.spi;

import com.microservicio.restaurant.domain.model.Order;
import com.microservicio.restaurant.domain.model.OrderDishes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IOrderPersistencePort {

    Order saveOrder(Order order);

    List<OrderDishes> saveOrderDishes(List<OrderDishes> orderDishes);

    boolean existsActiveOrdersByClient(Long clientId);

    List<Order> findOrdersByClientAndStatuses(Long clientId, List<String> statuses);

    Page<Order> findOrdersByStatus(String status, Pageable pageable);

    Order assignOrderToEmployee(Long orderId);

    Order findOrderById(Long orderId);

    Order updateOrderStatus(Order order);

    boolean isEmployeeAssignedToOrder(Long orderId, Long employeeId);

    Order deliverOrder(Long orderId, String securityPin);

    void cancelOrder(Long orderId, Long clientId);

}
