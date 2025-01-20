package com.microservicio.restaurant.infraestructure.output.jpa.mapper;

import com.microservicio.restaurant.domain.model.Order;
import com.microservicio.restaurant.domain.model.OrderDishes;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.OrderDishesEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.OrderEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.RestaurantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component

@RequiredArgsConstructor
public class OrderEntityMapper {

    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setIdClient(order.getIdClient());
        entity.setDate(order.getDate());
        entity.setStatus(order.getStatus());
        entity.setIdEmployee(order.getIdEmpleoyee());
        entity.setSecurityPin(order.getSecurityPin()); // Agregamos el securityPin

        // Buscar y setear la entidad del restaurante
        if (order.getIdRestaurant() != null) {
            RestaurantEntity restaurantEntity = new RestaurantEntity();
            restaurantEntity.setId(order.getIdRestaurant());
            entity.setRestaurant(restaurantEntity);
        }

        return entity;
    }

    public Order toModel(OrderEntity entity) {
        return new Order(
                entity.getId(),
                entity.getIdClient(),
                entity.getDate(),
                entity.getStatus(),
                entity.getIdEmployee(),
                entity.getRestaurant().getId(),
                entity.getSecurityPin()
        );
    }

    public OrderDishesEntity toOrderDishesEntity(OrderDishes orderDishes) {
        OrderDishesEntity entity = new OrderDishesEntity();
        entity.setId(orderDishes.getId());

        // Configurar las relaciones
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderDishes.getIdOrder());
        entity.setOrder(orderEntity);

        entity.setAmount(orderDishes.getAmount());
        return entity;
    }

    public OrderDishes toOrderDishesModel(OrderDishesEntity entity) {
        return new OrderDishes(
                entity.getId(),
                entity.getOrder().getId(),
                entity.getDish().getId(),
                entity.getAmount()
        );
    }
}