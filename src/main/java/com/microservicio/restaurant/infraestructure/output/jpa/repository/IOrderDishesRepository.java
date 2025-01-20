package com.microservicio.restaurant.infraestructure.output.jpa.repository;

import com.microservicio.restaurant.infraestructure.output.jpa.entity.OrderDishesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IOrderDishesRepository extends JpaRepository<OrderDishesEntity, Long> {

    List<OrderDishesEntity> findByOrder_Id(Long orderId);
}
