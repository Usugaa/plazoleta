package com.microservicio.restaurant.infraestructure.input.rest;

import com.microservicio.restaurant.application.dto.RestaurantRequest;
import com.microservicio.restaurant.application.dto.RestaurantResponse;
import com.microservicio.restaurant.application.dto.TraceabilityResponse;
import com.microservicio.restaurant.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurant Controller", description = "API para la gestión de restaurantes")
public class RestaurantController {

    private final IRestaurantHandler restaurantHandler;

    @PostMapping("/saveRestaurant")
    @Operation(
            summary = "Crear un nuevo restaurante",
            description = "Crea un nuevo restaurante en el sistema",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponse(
            responseCode = "201",
            description = "Restaurante creado exitosamente",
            content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
    )

    public ResponseEntity<RestaurantResponse> saveRestaurant(
            @RequestHeader("Authorization") String token,
            @RequestBody RestaurantRequest restaurantRequest
    ) {
        try {
            RestaurantResponse restaurantResponse = restaurantHandler.saveRestaurant(restaurantRequest);
            return new ResponseEntity<>(restaurantResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(RestaurantResponse.builder()
                            .message("Error al crear el restaurante: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/getRestaurants")
    @Operation(
            summary = "Obtener todos los restaurantes",
            description = "Obtiene una lista de todos los restaurantes registrados",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de restaurantes recuperada exitosamente",
            content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
    )
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        List<RestaurantResponse> restaurantResponses = restaurantHandler.getAllRestaurants();
        return new ResponseEntity<>(restaurantResponses, HttpStatus.OK);
    }

    @GetMapping("/efficiency/{restaurantId}")
    @Operation(
            summary = "Obtener eficiencia del restaurante",
            description = "Obtiene los datos de eficiencia de los pedidos del restaurante",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Datos de eficiencia recuperados exitosamente",
            content = @Content(schema = @Schema(implementation = TraceabilityResponse.class))
    )

    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
    )
    public ResponseEntity<List<TraceabilityResponse>> getRestaurantEfficiency(
            @PathVariable String restaurantId) {
        return ResponseEntity.ok(restaurantHandler.getRestaurantEfficiency(restaurantId));
    }
}
