package com.techtestinc.payment.services.service;

import com.techtestinc.payment.services.enums.PaymentType;

/**
 * @author yuvaraj.sanjeevi
 */
public interface PaymentService {

    void processPayment(String payload, PaymentType paymentType);

    void logErrorResponse(String paymentId, Exception exception);
}
