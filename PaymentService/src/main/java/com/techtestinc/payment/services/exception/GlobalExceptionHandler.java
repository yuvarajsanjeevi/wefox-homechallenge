package com.techtestinc.payment.services.exception;

import com.techtestinc.payment.services.payload.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleGenericException(Exception exception) {
        log.error("Exception Occurred ", exception);
        return buildApiErrorResponse(exception.getMessage(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AppException.class})
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(AppException exception) {
        log.error("AppException Exception Occurred ", exception);
        return new ResponseEntity<>(this.buildApiErrorResponse(exception.getMessage(), String.valueOf(exception.getStatus().value()), exception.getStatus()), exception.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleException(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValid Exception Occurred ", exception);
        List<String> errorMessages = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error -> errorMessages.add(((DefaultMessageSourceResolvable) Objects.requireNonNull(error.getArguments())[0]).getCode() + " " + error.getDefaultMessage()));
        return this.buildApiErrorResponse(errorMessages.toString(), String.valueOf(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.error("ResourceNotFoundException Exception Occurred ", exception);
        return this.buildApiErrorResponse(exception.getErrorMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String message = "Request method '" + ex.getMethod() + "' not supported. List of all supported methods - " + ex.getSupportedHttpMethods();
        return this.buildApiErrorResponse(message, String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "Please provide Request Body in valid JSON format";
        return this.buildApiErrorResponse(message, String.valueOf(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }


    private ApiErrorResponse buildApiErrorResponse(String message, String code, HttpStatus httpStatus) {
        return ApiErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(new Date())
                .status(httpStatus.value())
                .path(getUriPath())
                .build();
    }

    private String getUriPath() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return httpServletRequest.getRequestURI();
    }
}
