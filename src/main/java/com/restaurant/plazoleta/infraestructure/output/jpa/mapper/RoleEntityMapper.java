package com.restaurant.plazoleta.infraestructure.output.jpa.mapper;

import com.restaurant.plazoleta.domain.model.Role;
import com.restaurant.plazoleta.infraestructure.output.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RoleEntityMapper {

    RoleEntity toEntity(Role role);

    Role toDomain(RoleEntity roleEntity);
}