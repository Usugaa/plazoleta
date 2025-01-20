package com.restaurant.plazoleta.infraestructure.output.jpa.adapter;

import com.restaurant.plazoleta.domain.model.Role;
import com.restaurant.plazoleta.domain.spi.IRolePersistencePort;
import com.restaurant.plazoleta.infraestructure.output.jpa.entity.RoleEntity;
import com.restaurant.plazoleta.infraestructure.output.jpa.mapper.RoleEntityMapper;
import com.restaurant.plazoleta.infraestructure.output.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;
    private final RoleEntityMapper roleEntityMapper;
    @Override
    public Role saveRole(Role role) {
        RoleEntity roleEntity = roleEntityMapper.toEntity(role);
        RoleEntity saveEntity = roleRepository.save(roleEntity);
        return roleEntityMapper.toDomain(saveEntity);
    }
}