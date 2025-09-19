package com.zihan.small_inventory.utils;

public class ResponseUtil<T>  {
    private boolean success;
    private T data;
    private String message;

    // Constructors
    public ResponseUtil() {}

    public ResponseUtil(T data) {
        this.success = true;
        this.data = data;
    }

    public ResponseUtil(String error) {
        this.success = false;
        this.message = error;
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
