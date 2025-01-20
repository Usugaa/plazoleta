package com.restaurant.plazoleta.application.handler;

import com.restaurant.plazoleta.application.dto.RoleRequest;
import com.restaurant.plazoleta.application.dto.RoleResponse;

public interface IRoleHandler {

    RoleResponse saveRole(RoleRequest roleRequest);
}
