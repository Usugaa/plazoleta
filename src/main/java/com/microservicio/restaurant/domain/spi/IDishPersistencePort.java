package com.microservicio.restaurant.domain.spi;

import com.microservicio.restaurant.domain.model.Dish;

import java.util.List;
import java.util.Optional;


public interface IDishPersistencePort {

    Dish saveDish(Dish dish);

    Optional<Dish> findDishById(Long id);


    Dish updateDish(Long id, String description, Long price);

    Dish updateDishStatus(Long id, boolean active);

    List<Dish> findDishesByRestaurant(Long idRestaurant);
}