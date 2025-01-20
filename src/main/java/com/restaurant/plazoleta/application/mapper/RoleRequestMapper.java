package com.restaurant.plazoleta.application.mapper;

import com.restaurant.plazoleta.application.dto.RoleRequest;
import com.restaurant.plazoleta.domain.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface RoleRequestMapper {
    Role toDomain(RoleRequest roleRequest);
}