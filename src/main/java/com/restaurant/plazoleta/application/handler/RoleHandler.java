package com.restaurant.plazoleta.application.handler;

import com.restaurant.plazoleta.application.dto.RoleRequest;
import com.restaurant.plazoleta.application.dto.RoleResponse;
import com.restaurant.plazoleta.application.mapper.RoleRequestMapper;
import com.restaurant.plazoleta.application.mapper.RoleResponseMapper;
import com.restaurant.plazoleta.domain.api.IRoleServicePort;
import com.restaurant.plazoleta.domain.model.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleHandler implements IRoleHandler {

    private final IRoleServicePort roleServicePort;
    private final RoleRequestMapper roleRequestMapper;
    private final RoleResponseMapper roleResponseMapper;

    @Override
    public RoleResponse saveRole(RoleRequest roleRequest) {
        Role role = roleRequestMapper.toDomain(roleRequest);
        Role savedRol = roleServicePort.insertRole(role);
        return roleResponseMapper.toResponse(savedRol);
    }
}
