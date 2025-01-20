package com.restaurant.plazoleta.infraestructure.output.jpa.mapper;

import com.restaurant.plazoleta.domain.model.User;
import com.restaurant.plazoleta.infraestructure.output.jpa.entity.RoleEntity;
import com.restaurant.plazoleta.infraestructure.output.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "documentNumber", target = "documentNumber")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "birthdate", target = "birthdate") // Asegúrate de tener esta propiedad en ambos lados
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "idRole", target = "idRole", qualifiedByName = "longToRoleEntity")
    UserEntity toEntity(User user);

    @Mapping(source = "idRole", target = "idRole", qualifiedByName = "roleEntityToLong")
    User toDomain(UserEntity userEntity);

    // Métodos auxiliares para convertir entre Long y RoleEntity
    @Named("longToRoleEntity")
    default RoleEntity longToRoleEntity(Long idRole) {
        if (idRole == null) {
            return null;
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(idRole);
        return roleEntity;
    }

    @Named("roleEntityToLong")
    default Long roleEntityToLong(RoleEntity roleEntity) {
        return (roleEntity == null) ? null : roleEntity.getId();
    }
}
