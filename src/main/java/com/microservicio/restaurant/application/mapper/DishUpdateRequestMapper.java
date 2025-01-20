package com.microservicio.restaurant.application.mapper;

import com.microservicio.restaurant.application.dto.UpdateDishRequest;
import com.microservicio.restaurant.domain.model.Dish;
import org.springframework.stereotype.Component;

@Component

public class DishUpdateRequestMapper {

    public Dish toDishUpdate(UpdateDishRequest updateDishRequest) {
        Dish dish = new Dish();
        dish.setDescription(updateDishRequest.getDescription());
        dish.setPrice(updateDishRequest.getPrice());
        return dish;
    }
}
