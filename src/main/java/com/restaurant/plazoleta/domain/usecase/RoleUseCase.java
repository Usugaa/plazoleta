package com.restaurant.plazoleta.domain.usecase;

import com.restaurant.plazoleta.domain.api.IRoleServicePort;
import com.restaurant.plazoleta.domain.model.Role;
import com.restaurant.plazoleta.domain.spi.IRolePersistencePort;

public class RoleUseCase implements IRoleServicePort {

    private final IRolePersistencePort rolePersistencePort;

    public RoleUseCase(IRolePersistencePort rolePersistencePort) {
        this.rolePersistencePort = rolePersistencePort;
    }

    @Override
    public Role insertRole(Role role) {
        return null;
    }
}

