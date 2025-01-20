package com.microservicio.restaurant.domain.spi;

import com.microservicio.restaurant.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantePersistencePort {

    Restaurant saveRestaurant(Restaurant restaurant);

    List<Restaurant> findAllRestaurants();
}
