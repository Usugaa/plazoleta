package com.restaurant.plazoleta.application.mapper;

import com.restaurant.plazoleta.application.dto.UserRequest;
import com.restaurant.plazoleta.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper {

    User toDomain(UserRequest userRequest);

}