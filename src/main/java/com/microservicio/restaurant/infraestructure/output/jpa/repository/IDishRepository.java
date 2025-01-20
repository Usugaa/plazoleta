package com.microservicio.restaurant.infraestructure.output.jpa.repository;

import com.microservicio.restaurant.infraestructure.output.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    List<DishEntity> findByRestaurantId(Long idRestaurant);

}
