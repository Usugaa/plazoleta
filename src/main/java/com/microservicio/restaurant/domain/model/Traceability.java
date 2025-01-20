package com.microservicio.restaurant.domain.model;

import java.time.LocalDateTime;

public class Traceability {
    private Long id;
    private Long orderId;
    private String clientId;
    private String clientEmail;
    private LocalDateTime date;
    private String previousStatus;
    private String newStatus;
    private Long employeeId;
    private String employeeEmail;
    private String restaurantId;

    private Traceability() {
        this.date = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getRestaurantId(){
        return restaurantId;
    }

    public static class TraceabilityBuilder {
        private final Traceability traceability;

        public TraceabilityBuilder() {
            traceability = new Traceability();
        }

        public TraceabilityBuilder orderId(Long orderId) {
            traceability.orderId = orderId;
            return this;
        }

        public TraceabilityBuilder clientId(String clientId) {
            traceability.clientId = clientId;
            return this;
        }

        public TraceabilityBuilder clientEmail(String clientEmail) {
            traceability.clientEmail = clientEmail;
            return this;
        }

        public TraceabilityBuilder status(String previous, String newStatus) {
            traceability.previousStatus = previous;
            traceability.newStatus = newStatus;
            return this;
        }

        public TraceabilityBuilder employee(Long id, String email) {
            traceability.employeeId = id;
            traceability.employeeEmail = email;
            return this;
        }

        public TraceabilityBuilder restaurantId(String restaurantid) {
            traceability.restaurantId = restaurantid;
            return this;
        }

        public Traceability build() {
            return traceability;
        }
    }
}
