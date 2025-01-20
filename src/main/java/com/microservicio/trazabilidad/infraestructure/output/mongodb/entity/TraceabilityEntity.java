package com.microservicio.trazabilidad.infraestructure.output.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "traceability")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraceabilityEntity {

    @Id
    private String id;
    private Long orderId;
    private String clientId;
    private String clientEmail;
    private LocalDateTime date;
    private String previousStatus;
    private String newStatus;
    private Long employeeId;
    private String employeeEmail;
    private String restaurantId;

}
