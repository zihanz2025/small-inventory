package com.zihan.small_inventory.constants;

public final class ResponseCode {
    private ResponseCode() {}

    // Success
    public static final int SUCCESS = 200;

    // Auth
    public static final int REGISTRATION_FAILED = 400;
    public static final int LOGIN_FAILED = 401;


    // Shop related errors
    public static final int SHOP_ID_EXISTS = 401;
    public static final int SHOP_ID_NOT_FOUND = 402;
    public static final int SHOP_INVALID_PASSWORD = 403;
    public static final int SHOP_UNAUTHORIZED = 404;

    // Auth related errors
    public static final int AUTH_INVALID_TOKEN = 501;
    public static final int AUTH_EXPIRED_TOKEN = 502;

    // Generic errors
    public static final int INTERNAL_ERROR = 900;
}

