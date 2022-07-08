
package com.techtestinc.payment.services.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ApiErrorResponse {

    private String code;
    private String message;
    private String path;
    private Integer status;
    private Date timestamp;

}
