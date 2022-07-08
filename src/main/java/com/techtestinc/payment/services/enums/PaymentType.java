package com.techtestinc.payment.services.enums;

/**
 * @author yuvaraj.sanjeevi
 */
public enum PaymentType {

    ONLINE("online"),
    OFFLINE("offline");

    private String paymentType;

    PaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }
}
