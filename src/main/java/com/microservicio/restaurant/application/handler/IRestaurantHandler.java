package com.microservicio.restaurant.application.handler;

import com.microservicio.restaurant.application.dto.RestaurantRequest;
import com.microservicio.restaurant.application.dto.RestaurantResponse;
import com.microservicio.restaurant.application.dto.TraceabilityResponse;

import java.util.List;


public interface IRestaurantHandler {

    RestaurantResponse saveRestaurant(RestaurantRequest restaurantRequest);

    List<RestaurantResponse> getAllRestaurants();

    List<TraceabilityResponse> getRestaurantEfficiency(String restaurantId);
}
