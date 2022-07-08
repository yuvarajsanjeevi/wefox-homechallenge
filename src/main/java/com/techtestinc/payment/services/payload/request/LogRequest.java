package com.techtestinc.payment.services.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogRequest {

    @JsonProperty("payment_id")
    private String paymentId;
    @JsonProperty("error_description")
    private String errorDescription;
    @JsonProperty("error_type")
    private String errorType;

}
