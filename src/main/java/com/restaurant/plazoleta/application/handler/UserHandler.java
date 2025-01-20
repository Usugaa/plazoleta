package com.restaurant.plazoleta.application.handler;

import com.restaurant.plazoleta.application.dto.AuthRequest;
import com.restaurant.plazoleta.application.dto.AuthResponse;
import com.restaurant.plazoleta.application.dto.UserRequest;
import com.restaurant.plazoleta.application.dto.UserResponse;
import com.restaurant.plazoleta.application.mapper.UserRequestMapper;
import com.restaurant.plazoleta.application.mapper.UserResponseMapper;
import com.restaurant.plazoleta.domain.api.IUserServicePort;
import com.restaurant.plazoleta.domain.constants.RoleConstants;
import com.restaurant.plazoleta.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public UserResponse saveAdmin(UserRequest userRequest) {
        // Asignar rol de Administrador
        User user = userRequestMapper.toDomain(userRequest);
        user.setIdRole(RoleConstants.ADMINISTRADOR);
        User savedUser = userServicePort.saveUser(user);
        return userResponseMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse saveOwner(UserRequest userRequest) {
        // Asignar rol de Propietario
        User user = userRequestMapper.toDomain(userRequest);
        user.setIdRole(RoleConstants.PROPIETARIO);
        User savedUser = userServicePort.saveUser(user);
        return userResponseMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse saveEmployee(UserRequest userRequest) {
        // Asignar rol de Empleado
        User user = userRequestMapper.toDomain(userRequest);
        user.setIdRole(RoleConstants.EMPLEADO);
        User savedUser = userServicePort.saveUser(user);
        return userResponseMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse saveClient(UserRequest userRequest) {
        // Asignar rol de Cliente
        User user = userRequestMapper.toDomain(userRequest);
        user.setIdRole(RoleConstants.CLIENTE);
        User savedUser = userServicePort.saveUser(user);
        return userResponseMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userServicePort.getUserById(id);
        return userResponseMapper.toResponse(user);
    }

    public Long getRoleIdFromAuthenticatedUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userIdLong = Long.parseLong(userId);
        User user = userServicePort.getUserById(userIdLong);
        return user.getIdRole();
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {

        User user = userServicePort.authenticate(authRequest.getEmail(), authRequest.getPassword());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(user.getEmail());
        authResponse.setPassword(user.getPassword());

        return authResponse;
    }
}