package com.restaurant.plazoleta.domain.spi;

import com.restaurant.plazoleta.domain.model.User;

public interface IUserPersistencePort {

    User saveUser (User user);

    boolean existsByDocumentNumber(String documentNumber);

    User getByEmailAndPassword(String email, String password);

    User getByEmail(String email);

    User getUserById(Long id);

}