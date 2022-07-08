package com.techtestinc.payment.services.exception;

/**
 * @author yuvaraj.sanjeevi
 */
public class ResourceNotFoundException extends RuntimeException{

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super();
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public String getErrorMessage() {
        return String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue);
    }
}
