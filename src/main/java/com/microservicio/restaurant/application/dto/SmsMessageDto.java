package com.microservicio.restaurant.application.dto;

public record SmsMessageDto (
        Long orderId,
        String securityPin,
        String phoneNumber
) {}
