package com.microservicio.restaurant.domain.model;


public class SmsMessage {

    private final Long orderId;
    private final String securityPin;
    private final String phoneNumber;

    public SmsMessage(Long orderId, String securityPin, String phoneNumber) {
        this.orderId = orderId;
        this.securityPin = securityPin;
        this.phoneNumber = phoneNumber;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getSecurityPin() {
        return securityPin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
