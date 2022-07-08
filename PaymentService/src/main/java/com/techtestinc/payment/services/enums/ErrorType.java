package com.techtestinc.payment.services.enums;

/**
 * @author yuvaraj.sanjeevi
 */
public enum ErrorType {

    DATABASE("database"),
    NETWORK("network"),
    OTHER("other");

    private String errorType;

    ErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}
