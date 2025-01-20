package com.microservicio.restaurant.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDishStatusRequest {

    private boolean active;

}
