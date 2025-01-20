package com.restaurant.plazoleta.infraestructure.output.jpa.repository;

import com.restaurant.plazoleta.infraestructure.output.jpa.entity.UserEntity;
import org.hibernate.annotations.NotFound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByDocumentNumber(String documentNumber);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    @NotFound
    Optional<UserEntity> findByEmail(String email);
}