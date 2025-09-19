package com.zihan.small_inventory.exceptions;

public class ShopIdAlreadyExistsException extends RuntimeException {
    public ShopIdAlreadyExistsException(String shopId) {
        super("Shop ID already exists: " + shopId);
    }
}