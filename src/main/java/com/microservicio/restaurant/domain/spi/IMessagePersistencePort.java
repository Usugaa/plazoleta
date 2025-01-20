package com.microservicio.restaurant.domain.spi;

import com.microservicio.restaurant.domain.model.SmsMessage;

public interface IMessagePersistencePort {

    void sendOrderReadyMessage(SmsMessage smsMessage);

    void sendOrderDeliveredMessage(SmsMessage smsMessage);

}
