package com.microservicio.restaurant.application.mapper;

import com.microservicio.restaurant.application.dto.OrderDishRequest;
import com.microservicio.restaurant.application.dto.OrderDishResponse;
import com.microservicio.restaurant.domain.model.OrderDishes;

import java.util.ArrayList;
import java.util.List;

public class OrderDishesRequestMapper {

    public List<OrderDishes> toOrderDishes(List<OrderDishRequest> dishes, Long orderId) {
        List<OrderDishes> orderDishes = new ArrayList<>();
        for(OrderDishRequest dish : dishes) {
            orderDishes.add(new OrderDishes(
                    null,
                    orderId,
                    dish.getIdDish(),
                    dish.getAmount()
            ));
        }
        return orderDishes;
    }

    public List<OrderDishResponse> toOrderDishesResponse(List<OrderDishes> dishes) {
        List<OrderDishResponse> responses = new ArrayList<>();
        for(OrderDishes dish : dishes) {
            OrderDishResponse response = new OrderDishResponse();
            response.setId(dish.getId());
            response.setIdOrder(dish.getIdOrder());
            response.setIdDish(dish.getIdDish());
            response.setAmount(dish.getAmount());
            responses.add(response);
        }
        return responses;
    }
}