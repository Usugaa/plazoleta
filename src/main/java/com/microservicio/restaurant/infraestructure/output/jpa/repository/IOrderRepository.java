package com.microservicio.restaurant.infraestructure.output.jpa.repository;

import com.microservicio.restaurant.infraestructure.output.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByIdClientAndStatusIn(Long idClient, List<String> statuses);

    Page<OrderEntity> findByStatus(String status, Pageable pageable);
}