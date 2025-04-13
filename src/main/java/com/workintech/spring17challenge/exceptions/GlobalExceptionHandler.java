package com.workintech.spring17challenge.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleException(ApiException apiException) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(apiException.getHttpStatus().value(), apiException.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, apiException.getHttpStatus());
    }
}
