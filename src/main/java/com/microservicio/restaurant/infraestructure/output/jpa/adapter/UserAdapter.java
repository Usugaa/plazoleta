package com.microservicio.restaurant.infraestructure.output.jpa.adapter;

import com.microservicio.restaurant.application.dto.UserDTO;
import com.microservicio.restaurant.domain.constants.RoleConstants;
import com.microservicio.restaurant.domain.spi.IUserPersistencePort;
import com.microservicio.restaurant.infraestructure.input.client.UserFeignClient;
import com.microservicio.restaurant.infraestructure.output.jpa.exceptions.EntityNotFoundException;
import com.microservicio.restaurant.infraestructure.output.jpa.exceptions.InvalidRoleException;
import com.microservicio.restaurant.infraestructure.output.jpa.exceptions.RestaurantPersistenceException;
import feign.FeignException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAdapter implements IUserPersistencePort {

    private final UserFeignClient userFeignClient;

    @Override
    public Long getUserById(Long id) {
        try {
            UserDTO user = userFeignClient.getUserById(id);

            if (user == null || user.getId() == null) {
                throw new EntityNotFoundException("No se pudo obtener la información del usuario con ID: " + id);
            }
            if (user.getIdRole() == null || !RoleConstants.OWNER.equals(user.getIdRole())) {
                throw new InvalidRoleException("Solo los usuarios con rol PROPIETARIO pueden crear restaurantes");
            }

            return user.getId();

        } catch (FeignException e) {
            e.printStackTrace();
            throw new RestaurantPersistenceException("Error en la comunicación con el servicio de usuarios: " + e.getMessage());
        }
    }
}