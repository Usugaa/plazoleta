package com.microservicio.restaurant.infraestructure.input.rest;

import com.microservicio.restaurant.application.dto.DishRequest;
import com.microservicio.restaurant.application.dto.DishResponse;
import com.microservicio.restaurant.application.dto.UpdateDishRequest;
import com.microservicio.restaurant.application.dto.UpdateDishStatusRequest;
import com.microservicio.restaurant.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "DishEndPoint")
@RestController
@RequestMapping("/platos")
@RequiredArgsConstructor

public class DishController {

    private final IDishHandler dishHandler;

    @Operation(
            summary = "Crear un nuevo plato",
            description = "Registra un nuevo plato en el sistema"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Plato creado exitosamente",
            content = @Content(schema = @Schema(implementation = DishResponse.class))
    )
    @PostMapping
    public ResponseEntity<DishResponse> saveDish(@RequestBody DishRequest dishRequest) {
        return ResponseEntity.ok(dishHandler.saveDish(dishRequest));
    }


    @Operation(
            summary = "Obtener plato por ID",
            description = "Recupera la información de un plato específico por su ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Plato encontrado",
                    content = @Content(schema = @Schema(implementation = DishResponse.class))
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Plato no encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DishResponse> getDishById(@PathVariable Long id) {
        Optional<DishResponse> dishResponse = dishHandler.findDishById(id);
        return dishResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Actualizar descripción y precio de plato",
            description = "Modifica la descripción y el precio de un plato existente"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Plato actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = DishResponse.class))
    )
    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> updateDishDescriptionAndPrice(
            @PathVariable Long id,
            @RequestBody UpdateDishRequest updateDishRequest) {
        DishResponse response = dishHandler.updateDishDescriptionAndPrice(id, updateDishRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar estado de plato",
            description = "Modifica el estado de un plato existente"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Estado del plato actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = DishResponse.class))
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<DishResponse> updateDishStatus(@PathVariable Long id, @RequestBody UpdateDishStatusRequest updateDishStatusRequest) {
        DishResponse updatedDish = dishHandler.updateDishStatus(id, updateDishStatusRequest);
        return ResponseEntity.ok(updatedDish);
    }

    @Operation(
            summary = "Obtener platos por restaurante",
            description = "Recupera todos los platos asociados a un restaurante específico"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de platos recuperada exitosamente",
            content = @Content(schema = @Schema(implementation = DishResponse.class))
    )
    @GetMapping("/restaurant/{idRestaurant}")
    public ResponseEntity<List<DishResponse>> getDishesByRestaurant(@PathVariable Long idRestaurant) {
        List<DishResponse> dishes = dishHandler.getDishesByRestaurant(idRestaurant);
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }
}