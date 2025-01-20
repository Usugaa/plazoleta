package com.microservicio.restaurant.infraestructure.input.client;

import com.microservicio.restaurant.infraestructure.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mensajeria", url = "http://localhost:8084", configuration = FeignConfiguration.class)
public interface SmsFeignClient {

    @PostMapping("/sms/notify/{orderId}")
    ResponseEntity<Void> sendOrderReadyNotification(
            @PathVariable Long orderId,
            @RequestParam String securityPin
    );

    @GetMapping("/sms/notify/delivered/{orderId}")
    ResponseEntity<Void> sendOrderDeliveredNotification(@PathVariable Long orderId);
}
