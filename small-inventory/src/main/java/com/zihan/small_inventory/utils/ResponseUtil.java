package com.zihan.small_inventory.utils;

import com.zihan.small_inventory.constants.ResponseCode;

public class ResponseUtil<T> {
    private T data;
    private String message;
    private int code; // 200 for success, fail otherwise.

    public ResponseUtil() {}

    public ResponseUtil(T data) {
        this.data = data;
        this.code = ResponseCode.SUCCESS;
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
        return this.code == ResponseCode.SUCCESS;
    }

    public boolean validateCode(int code){
        return this.code == code;
    }
}
