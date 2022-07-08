package com.techtestinc.payment.services.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtestinc.payment.services.entity.Account;
import com.techtestinc.payment.services.entity.Payment;
import com.techtestinc.payment.services.enums.ErrorType;
import com.techtestinc.payment.services.enums.PaymentType;
import com.techtestinc.payment.services.exception.AppException;
import com.techtestinc.payment.services.feign.ApiClient;
import com.techtestinc.payment.services.payload.request.LogRequest;
import com.techtestinc.payment.services.payload.request.PaymentRequest;
import com.techtestinc.payment.services.repository.PaymentRepository;
import com.techtestinc.payment.services.service.AccountService;
import com.techtestinc.payment.services.service.PaymentService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.Date;
import java.util.Optional;

/**
 * @author yuvaraj.sanjeevi
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ObjectMapper objectMapper;
    private final ApiClient apiClient;
    private final AccountService accountService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(String payload, PaymentType paymentType) {
        PaymentRequest paymentRequest = null;
        try {
            paymentRequest = objectMapper.readValue(payload, PaymentRequest.class);
            if (paymentType == PaymentType.ONLINE) {
                validatePaymentRequest(paymentRequest);
            }
            savePayment(paymentRequest);
        } catch (Exception ex) {
            String paymentId = Optional.ofNullable(paymentRequest).map(PaymentRequest::getPaymentId).orElse("");
            logErrorResponse(paymentId, ex);
        }
    }


    private void savePayment(PaymentRequest paymentRequest) {
        if (paymentRepository.existsById(paymentRequest.getPaymentId())) {
            String errorMessage = String.format("Duplicate payment id %s", paymentRequest.getPaymentId());
            throw new AppException(HttpStatus.CONFLICT, errorMessage);
        }

        Account account = accountService.findByAccountId(paymentRequest.getAccountId());
        account.setLastPaymentDate(new Date());
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentRequest, payment);
        payment.setAccount(account);
        payment.setCreditCard(paymentRequest.getCreditCard().replaceAll("(?<!^..).(?=.{3})", "*"));
        paymentRepository.save(payment);
    }

    /**
     * This method is used to validate the payment request
     * @param paymentRequest    - payment request
     */
    private void validatePaymentRequest(PaymentRequest paymentRequest) {
        apiClient.validatePaymentRequest(paymentRequest);
    }


    /**
     * This method is used to log the error response in the external system
     * @param paymentId         - payment id
     * @param exception         - exception
     */
    @Override
    public void logErrorResponse(String paymentId, Exception exception) {
        LogRequest logRequest;
        if (exception instanceof DataAccessException) {
            logRequest = LogRequest.builder().paymentId(paymentId).errorType(ErrorType.DATABASE.getErrorType()).errorDescription(exception.getMessage()).build();
        } else if (exception instanceof FeignException) {
            logRequest = LogRequest.builder().paymentId(paymentId).errorType(ErrorType.NETWORK.getErrorType()).errorDescription(exception.getMessage()).build();
        } else {
            logRequest = LogRequest.builder().paymentId(paymentId).errorType(ErrorType.OTHER.getErrorType()).errorDescription(exception.getMessage()).build();
        }
        try {
            apiClient.logResponse(logRequest);
        } catch (Exception ex) {
            log.error("Unable to log the request", ex);
        }
    }
}
