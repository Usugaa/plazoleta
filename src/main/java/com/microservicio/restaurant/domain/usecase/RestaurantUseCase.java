package com.microservicio.restaurant.domain.usecase;

import com.microservicio.restaurant.domain.api.IRestaurantServicePort;
import com.microservicio.restaurant.domain.model.Restaurant;
import com.microservicio.restaurant.domain.model.Traceability;
import com.microservicio.restaurant.domain.spi.IRestaurantePersistencePort;
import com.microservicio.restaurant.domain.spi.ITraceabilityPersistencePort;
import com.microservicio.restaurant.domain.spi.IUserPersistencePort;
import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IUserPersistencePort userPersistencePort;
    private final ITraceabilityPersistencePort traceabilityPersistencePort;

    public RestaurantUseCase(
            IRestaurantePersistencePort restaurantePersistencePort,
            IUserPersistencePort userPersistencePort,
            ITraceabilityPersistencePort traceabilityPersistencePort) {
        this.restaurantePersistencePort = restaurantePersistencePort;
        this.userPersistencePort = userPersistencePort;
        this.traceabilityPersistencePort = traceabilityPersistencePort;
    }


    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        if (restaurant.getIdOwner() == null) {
            throw new IllegalArgumentException("El ID del propietario es requerido");
        }

        Long idOwner = userPersistencePort.getUserById(restaurant.getIdOwner());

        restaurant.setIdOwner(idOwner);

        return restaurantePersistencePort.saveRestaurant(restaurant);
    }


    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantePersistencePort.findAllRestaurants();
    }

    @Override
    public List<Traceability> getRestaurantEfficiency(String restaurantId) {
        return traceabilityPersistencePort.findByRestaurantId(restaurantId);
    }
}