package com.example.foruminforexchange.Exception;

public class AppException extends RuntimeException{

    public AppException(ErrorCode errorCode){
        super();
        this.errorCode = errorCode;
    }

    public AppException(){

    }

    private ErrorCode errorCode;

    public ErrorCode getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
