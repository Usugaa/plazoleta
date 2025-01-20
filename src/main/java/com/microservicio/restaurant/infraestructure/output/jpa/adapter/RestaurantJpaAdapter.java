package com.microservicio.restaurant.infraestructure.output.jpa.adapter;

import com.microservicio.restaurant.domain.model.Restaurant;
import com.microservicio.restaurant.domain.spi.IRestaurantePersistencePort;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.RestaurantEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.exceptions.EntityNotFoundException;
import com.microservicio.restaurant.infraestructure.output.jpa.exceptions.RestaurantPersistenceException;
import com.microservicio.restaurant.infraestructure.output.jpa.mapper.RestaurantEntityMapper;
import com.microservicio.restaurant.infraestructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantePersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        try {
            RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurant);
            RestaurantEntity savedEntity = restaurantRepository.save(restaurantEntity);
            return restaurantEntityMapper.toDomain(savedEntity);
        } catch (DataIntegrityViolationException e) {
            throw new RestaurantPersistenceException("Error al guardar el restaurante: violación de integridad de datos");
        } catch (Exception e) {
            throw new RestaurantPersistenceException("Error al guardar el restaurante: " + e.getMessage());
        }
    }

    @Override
    public List<Restaurant> findAllRestaurants() {
        try {
            List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();

            if (restaurantEntities.isEmpty()) {
                throw new EntityNotFoundException("No se encontraron restaurantes");
            }

            return restaurantEntities.stream()
                    .map(restaurantEntityMapper::toDomain)
                    .toList();
        } catch (EntityNotFoundException e) {
            throw e;  // Re-lanzamos la excepción para que la maneje el ControllerAdvisor
        } catch (Exception e) {
            throw new RestaurantPersistenceException("Error al obtener los restaurantes: " + e.getMessage());
        }
    }
}