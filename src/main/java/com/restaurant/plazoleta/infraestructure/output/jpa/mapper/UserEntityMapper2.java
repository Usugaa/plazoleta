package com.restaurant.plazoleta.infraestructure.output.jpa.mapper;

import com.restaurant.plazoleta.domain.model.User;
import com.restaurant.plazoleta.infraestructure.output.jpa.entity.UserEntity;

public class UserEntityMapper2 {

    public static User toDomain(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getLastName(),
                userEntity.getDocumentNumber(),
                userEntity.getPhone(),
                userEntity.getBirthdate(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getIdRole().getId()
        );

    }

}
