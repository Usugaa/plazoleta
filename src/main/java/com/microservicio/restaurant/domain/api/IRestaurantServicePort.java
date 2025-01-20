package com.microservicio.restaurant.domain.api;

import com.microservicio.restaurant.domain.model.Restaurant;
import com.microservicio.restaurant.domain.model.Traceability;

import java.util.List;


public interface IRestaurantServicePort {

    Restaurant saveRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();

    List<Traceability> getRestaurantEfficiency(String restaurantId);

}
