package com.microservicio.restaurant.domain.spi;

public interface IUserPersistencePort {

    Long getUserById(Long id);

}