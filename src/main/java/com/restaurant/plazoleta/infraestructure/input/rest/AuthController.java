package com.restaurant.plazoleta.infraestructure.input.rest;

import com.restaurant.plazoleta.application.dto.UserResponse;
import com.restaurant.plazoleta.application.handler.IUserHandler;
import com.restaurant.plazoleta.infraestructure.security.AuthManager;
import com.restaurant.plazoleta.infraestructure.security.CustomUserDetails;
import com.restaurant.plazoleta.infraestructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and user retrieval")
public class AuthController {

    private final IUserHandler userHandler;
    private final AuthManager authManager;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user and generate JWT token")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @ApiResponse(responseCode = "400", description = "Incorrect Authorization header")
    public ResponseEntity<String> login(
            @Parameter(description = "HTTP Servlet Request with Basic Authentication", required = true)
            HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);

            String email = values[0];
            String password = values[1];

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            final CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

            if (customUserDetails != null) {
                final String jwt = jwtService.generateTokenWithUserInfo(customUserDetails);
                return ResponseEntity.ok(jwt);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("UserDetails instance is invalid");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing or incorrect");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve user information by user ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "JWT Bearer Token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "User ID", required = true)
            @PathVariable("id") Long id) {
        // (Método original sin cambios en la lógica)
        try {
            if (!token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token debe comenzar con 'Bearer'");
            }

            String jwt = token.substring(7);
            if (!jwtService.isTokenValid(jwt)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token inválido o expirado");
            }

            UserResponse userResponse = userHandler.getUserById(id);
            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener información del usuario: " + e.getMessage());
        }
    }
}