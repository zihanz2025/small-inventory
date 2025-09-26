package com.zihan.small_inventory.utils;

import com.zihan.small_inventory.constants.ResponseCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class exceptionUtil {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseUtil<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseUtil<>("Forbidden: " + ex.getMessage(), ResponseCode.AUTH_ROLE_FAILED);
    }
}


