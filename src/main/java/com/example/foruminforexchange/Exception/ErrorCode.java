package com.example.foruminforexchange.Exception;

public enum ErrorCode {
    EMAIL_PASSWORD_NOT_TRUE(999, "EMAIL OR PASSWORD IS NOT CORRECT"),
    NOT_FOUND(1000, "NOT FOUND"),
    USER_EXISTED(1001, "User existed"),

    USER_NOT_FOUND(1002, "User not found!"),

    POST_NOT_FOUND(1003, "Post not found!"),

    COMMENT_NOT_FOUND(1004, "Comment not found" ),


    REPORT_EXISTED(1005, "Cannot report second time"),

    NOT_ADMIN(1006, "You are not admin"),
    EXPIRED_TOKEN(1007, "EXPIRED TOKEN"),
    NOT_FOUND_TOPIC(1008, "NOT FOUND TOPIC"),
    NOT_BLANK(1009, "FIELD MUST'NT BLANK")
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
