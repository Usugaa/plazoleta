package com.restaurant.plazoleta.infraestructure.input.rest;

import com.restaurant.plazoleta.application.dto.RoleRequest;
import com.restaurant.plazoleta.application.dto.RoleResponse;
import com.restaurant.plazoleta.application.handler.IRoleHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleHandler roleHandler;

    @PostMapping
    public ResponseEntity<RoleResponse> saveRole(@RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleHandler.saveRole(roleRequest);
        return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
    }
}