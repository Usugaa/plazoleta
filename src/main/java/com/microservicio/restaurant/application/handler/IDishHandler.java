package com.microservicio.restaurant.application.handler;

import com.microservicio.restaurant.application.dto.DishRequest;
import com.microservicio.restaurant.application.dto.DishResponse;
import com.microservicio.restaurant.application.dto.UpdateDishRequest;
import com.microservicio.restaurant.application.dto.UpdateDishStatusRequest;

import java.util.List;
import java.util.Optional;


public interface IDishHandler {


    DishResponse saveDish(DishRequest dishRequest);

    DishResponse updateDishDescriptionAndPrice(Long id, UpdateDishRequest updateDishRequest);

    Optional<DishResponse> findDishById(Long id);

    DishResponse updateDishStatus(Long id, UpdateDishStatusRequest updateDishStatusRequest);

    List<DishResponse> getDishesByRestaurant(Long idRestaurant);
}