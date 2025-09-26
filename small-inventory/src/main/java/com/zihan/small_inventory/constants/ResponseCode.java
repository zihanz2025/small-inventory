package com.zihan.small_inventory.constants;

public final class ResponseCode {
    private ResponseCode() {}

    // Success
    public static final int SUCCESS = 200;

    // Auth
    public static final int REGISTRATION_FAILED = 400;
    public static final int LOGIN_FAILED = 401;

    // Shop related errors
    public static final int SHOP_ID_NOT_FOUND = 401;
    public static final int SHOP_INVALID_PASSWORD = 402;
    public static final int SHOP_UPDATE_FAILED = 403;

    // Auth related errors
    public static final int AUTH_TOKEN_FAILED = 501;
    public static final int AUTH_ROLE_FAILED = 502;


    // Generic errors
    public static final int INTERNAL_ERROR = 900;
}

