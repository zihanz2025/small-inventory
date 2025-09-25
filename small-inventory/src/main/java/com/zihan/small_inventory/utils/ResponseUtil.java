package com.zihan.small_inventory.utils;

public class ResponseUtil<T> {
    private T data;
    private String message;
    private int code; // 200 for success, fail otherwise.

    public ResponseUtil() {}

    public ResponseUtil(T data) {
        this.data = data;
        this.code = 200;
        this.message = "Success";
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

    public int getCode() { return code; }
    public void setCode(int code) {this.code = code;}

    public boolean isSuccess(){
        return this.code==200;
    }

    public boolean validateCode(int code){
        return this.code==code;
    }
}
