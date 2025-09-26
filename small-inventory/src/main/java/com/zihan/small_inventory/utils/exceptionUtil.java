package com.zihan.small_inventory.utils;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class exceptionUtil {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseUtil<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseUtil<>("Forbidden: " + ex.getMessage(), 301);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseUtil<Map<String, Object>> handleAuthentication(AuthenticationException ex) {
        return new ResponseUtil<>("Unauthorized: " + ex.getMessage(), 303);
    }
}


