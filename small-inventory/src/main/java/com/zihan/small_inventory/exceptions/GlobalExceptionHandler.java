package com.zihan.small_inventory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShopIdAlreadyExistsException.class)
    public ResponseEntity<?> handleShopIdAlreadyExists(ShopIdAlreadyExistsException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "error", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // You can add more handlers for other exceptions
}
