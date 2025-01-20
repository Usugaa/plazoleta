package com.restaurant.plazoleta.infraestructure.input.rest;

import com.restaurant.plazoleta.application.dto.UserRequest;
import com.restaurant.plazoleta.application.dto.UserResponse;
import com.restaurant.plazoleta.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for creating and retrieving users")
public class UserController {

    private final IUserHandler userHandler;

    @PostMapping("/admin")
    @Operation(summary = "Create Admin User", description = "Create a new admin user")
    @ApiResponse(responseCode = "201", description = "Admin user created successfully")
    public ResponseEntity<UserResponse> saveAdmin(
            @Parameter(description = "User details", required = true)
            @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userHandler.saveAdmin(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/owner")
    @Operation(summary = "Create Owner User", description = "Create a new owner user")
    @ApiResponse(responseCode = "201", description = "Owner user created successfully")
    public ResponseEntity<UserResponse> saveOwner(
            @Parameter(description = "User details", required = true)
            @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userHandler.saveOwner(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/employee")
    @Operation(summary = "Create Employee User", description = "Create a new employee user")
    @ApiResponse(responseCode = "201", description = "Employee user created successfully")
    public ResponseEntity<UserResponse> saveEmployee(
            @Parameter(description = "User details", required = true)
            @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userHandler.saveEmployee(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/client")
    @Operation(summary = "Create Client User", description = "Create a new client user")
    @ApiResponse(responseCode = "201", description = "Client user created successfully")
    public ResponseEntity<UserResponse> saveClient(
            @Parameter(description = "User details", required = true)
            @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userHandler.saveClient(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve user information by user ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable("id") Long id) {
        try {
            UserResponse response = userHandler.getUserById(id);
            if (response == null || response.getId() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}