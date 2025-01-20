package com.microservicio.restaurant.application.handler;

import com.microservicio.restaurant.application.dto.RestaurantRequest;
import com.microservicio.restaurant.application.dto.RestaurantResponse;
import com.microservicio.restaurant.application.dto.TraceabilityResponse;
import com.microservicio.restaurant.application.mapper.DishResponseMapper;
import com.microservicio.restaurant.application.mapper.RestaurantRequestMapper;
import com.microservicio.restaurant.application.mapper.RestaurantResponseMapper;
import com.microservicio.restaurant.application.mapper.TraceabilityMapper;
import com.microservicio.restaurant.domain.api.IRestaurantServicePort;
import com.microservicio.restaurant.domain.model.Restaurant;
import com.microservicio.restaurant.domain.model.Traceability;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler{

    private final IRestaurantServicePort restaurantServicePort;
    private final DishResponseMapper dishResponseMapper;
    private final RestaurantRequestMapper restaurantRequestMapper;
    private final RestaurantResponseMapper restaurantResponseMapper;
    private final TraceabilityMapper traceabilityMapper;

    @Override
    public RestaurantResponse saveRestaurant(RestaurantRequest request) {
        Restaurant restaurant = restaurantRequestMapper.toDomain(request);
        return restaurantResponseMapper.toResponse(restaurantServicePort.saveRestaurant(restaurant));
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants() {
        // Obtenemos todos los restaurantes desde el caso de uso
        List<Restaurant> restaurants = restaurantServicePort.getAllRestaurants();

        // Mapeamos a RestaurantResponse y los retornamos
        return restaurants.stream()
                .map(restaurantResponseMapper::toResponse)
                .toList();
    }

    @Override
    public List<TraceabilityResponse> getRestaurantEfficiency(String restaurantId) {
        List<Traceability> traceabilityList = restaurantServicePort.getRestaurantEfficiency(restaurantId);
        return traceabilityMapper.toResponseList(traceabilityList);
    }
}
