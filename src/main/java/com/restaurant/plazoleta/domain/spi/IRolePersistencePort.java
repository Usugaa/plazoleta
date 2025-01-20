package com.restaurant.plazoleta.domain.spi;

import com.restaurant.plazoleta.domain.model.Role;

public interface IRolePersistencePort {

    Role saveRole(Role role);
}