package com.techtestinc.payment.services.feign;

import com.techtestinc.payment.services.payload.request.LogRequest;
import com.techtestinc.payment.services.payload.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yuvaraj.sanjeevi
 */
@FeignClient(name = "api-client", url = "${api-url}", decode404 = true)
public interface ApiClient {

    @PostMapping("/payment")
    void validatePaymentRequest(@RequestBody PaymentRequest paymentRequest);

    @PostMapping("/log")
    void logResponse(@RequestBody LogRequest logRequest);
}
