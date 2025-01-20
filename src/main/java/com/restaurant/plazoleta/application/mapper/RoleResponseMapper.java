package com.restaurant.plazoleta.application.mapper;

import com.restaurant.plazoleta.application.dto.RoleResponse;
import com.restaurant.plazoleta.domain.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RoleResponseMapper {

    RoleResponse toResponse(Role role);
}