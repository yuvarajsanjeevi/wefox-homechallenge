package com.techtestinc.payment.services.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {

    @JsonProperty("payment_type")
    private String paymentType;
    @JsonProperty("credit_card")
    private String creditCard;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("payment_id")
    private String paymentId;

}
