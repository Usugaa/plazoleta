package com.microservicio.restaurant.infraestructure.output.jpa.adapter;

import com.microservicio.restaurant.domain.model.SmsMessage;
import com.microservicio.restaurant.domain.spi.IMessagePersistencePort;
import com.microservicio.restaurant.infraestructure.input.client.SmsFeignClient;
import com.microservicio.restaurant.infraestructure.output.jpa.exceptions.RestaurantPersistenceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class SmsMessageAdapter implements IMessagePersistencePort {

    private final SmsFeignClient smsFeignClient;

    @Override
    public void sendOrderReadyMessage(SmsMessage smsMessage) {
        try {
            smsFeignClient.sendOrderReadyNotification(
                    smsMessage.getOrderId(),
                    smsMessage.getSecurityPin()
            );
            log.info("Notificación SMS enviada para orden: {}", smsMessage.getOrderId());
        } catch (FeignException e) {
            log.error("Error al enviar notificación SMS: {}", e.getMessage());
            throw new RuntimeException("Error al enviar notificación SMS", e);
        }
    }

    @Override
    public void sendOrderDeliveredMessage(SmsMessage smsMessage) {
        try {
            log.info("Enviando notificación de entrega para el pedido: {}", smsMessage.getOrderId());
            smsFeignClient.sendOrderDeliveredNotification(smsMessage.getOrderId());
            log.info("Notificación de entrega enviada exitosamente para el pedido: {}", smsMessage.getOrderId());
        } catch (FeignException e) {
            log.error("Error al enviar notificación de entrega: {}", e.getMessage());
            throw new RestaurantPersistenceException("Error en la comunicación con el servicio de mensajería: " + e.getMessage());
        }
    }
}