package com.zihan.small_inventory.utils;

public class ResponseUtil<T> {
    private T data;
    private String message;
    private int code; // 200 for success, etc.

    public ResponseUtil() {}

    public ResponseUtil(T data) {
        this.data = data;
        this.code = 200;
    }

    public ResponseUtil(String message, int code) {
        this.message = message;
        this.code = code;
    }

    // Getters and setters

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
