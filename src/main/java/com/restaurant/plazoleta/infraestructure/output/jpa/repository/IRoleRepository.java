package com.restaurant.plazoleta.infraestructure.output.jpa.repository;

import com.restaurant.plazoleta.infraestructure.output.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    @Override
    Optional<RoleEntity> findById(Long id);
}