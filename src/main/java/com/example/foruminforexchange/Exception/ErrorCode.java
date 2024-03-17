package com.example.foruminforexchange.Exception;

public enum ErrorCode {
    EMAIL_PASSWORD_NOT_TRUE(999, "EMAIL OR PASSWORD IS NOT CORRECT"),
    NOT_FOUND(1000, "NOT FOUND"),
    USER_EXISTED(1001, "User existed")

    ;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    ErrorCode(){

    }
    private int code;
    private String message;
}
