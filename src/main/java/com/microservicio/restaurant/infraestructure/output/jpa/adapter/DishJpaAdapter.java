package com.microservicio.restaurant.infraestructure.output.jpa.adapter;

import com.microservicio.restaurant.domain.model.Dish;
import com.microservicio.restaurant.domain.spi.IDishPersistencePort;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.DishEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.RestaurantEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.mapper.DishEntityMapper;
import com.microservicio.restaurant.infraestructure.output.jpa.repository.IDishRepository;
import com.microservicio.restaurant.infraestructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;
    private final JwtService jwtService;

    @Override
    public Dish saveDish(Dish dish) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dish);
        DishEntity savedEntity = dishRepository.save(dishEntity);
        return dishEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Dish> findDishById(Long id) {
        Optional<DishEntity> dishEntityOptional = dishRepository.findById(id);
        return dishEntityOptional.map(dishEntityMapper::toDomain);
    }

    @Override
    public Dish updateDish(Long id, String description, Long price) {
        Optional<DishEntity> optionalDishEntity = dishRepository.findById(id);
        if (optionalDishEntity.isEmpty()) {
            return null;
        }

        DishEntity dishEntity = optionalDishEntity.get();
        RestaurantEntity restaurantEntity = dishEntity.getRestaurant();
        if (restaurantEntity == null) {
            return null;
        }

        // Obtener el token del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        // Obtener el token
        String token = (String) authentication.getCredentials();
        if (token == null) {
            return null;
        }

        // Extraer userId del token
        Long currentUserId;
        try {
            currentUserId = jwtService.extractUserId(token);
        } catch (Exception e) {
            return null;
        }

        // Validar que sea el owner del restaurante
        if (currentUserId == null || !restaurantEntity.getIdOwner().equals(currentUserId)) {
            return null;
        }

        dishEntity.setDescription(description);
        dishEntity.setPrice(price);

        DishEntity savedEntity = dishRepository.save(dishEntity);
        return dishEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Dish updateDishStatus(Long id, boolean active) {
        Optional<DishEntity> optionalDishEntity = dishRepository.findById(id);
        if (optionalDishEntity.isEmpty()) {
            return null;
        }

        DishEntity dishEntity = optionalDishEntity.get();
        RestaurantEntity restaurantEntity = dishEntity.getRestaurant();
        if (restaurantEntity == null) {
            return null;
        }

        // Obtener el token del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        // Obtener el token
        String token = (String) authentication.getCredentials();
        if (token == null) {
            return null;
        }

        // Extraer userId del token
        Long currentUserId;
        try {
            currentUserId = jwtService.extractUserId(token);
        } catch (Exception e) {
            return null;
        }

        // Validar que sea el owner del restaurante
        if (currentUserId == null || !restaurantEntity.getIdOwner().equals(currentUserId)) {
            return null;
        }

        dishEntity.setActive(active);
        DishEntity savedEntity = dishRepository.save(dishEntity);
        return dishEntityMapper.toDomain(savedEntity);
    }

    @Override
    public List<Dish> findDishesByRestaurant(Long idRestaurant) {
        List<DishEntity> dishEntities = dishRepository.findByRestaurantId(idRestaurant);
        return dishEntities.stream()
                .map(dishEntityMapper::toDomain)
                .toList();
    }
}
