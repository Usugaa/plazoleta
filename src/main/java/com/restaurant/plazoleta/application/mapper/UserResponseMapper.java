package com.restaurant.plazoleta.application.mapper;

import com.restaurant.plazoleta.application.dto.UserResponse;
import com.restaurant.plazoleta.domain.model.User;

public class UserResponseMapper {
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setLastName(user.getLastName());
        userResponse.setDocumentNumber(user.getDocumentNumber());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setIdRole(user.getIdRole());
        return userResponse;
    }
}