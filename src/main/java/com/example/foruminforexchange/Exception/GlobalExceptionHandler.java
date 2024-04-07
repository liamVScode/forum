package com.example.foruminforexchange.Exception;

import com.example.foruminforexchange.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        switch (errorCode) {
            case USER_EXISTED:
                apiResponse.setCode(errorCode.USER_EXISTED.getCode());
                apiResponse.setMessage(errorCode.USER_EXISTED.getMessage());
                break;
            case EMAIL_PASSWORD_NOT_TRUE:
                apiResponse.setCode(errorCode.EMAIL_PASSWORD_NOT_TRUE.getCode());
                apiResponse.setMessage(errorCode.EMAIL_PASSWORD_NOT_TRUE.getMessage());
                break;
            case NOT_FOUND:
                apiResponse.setCode(errorCode.NOT_FOUND.getCode());
                apiResponse.setMessage(errorCode.NOT_FOUND.getMessage());
                break;
            case USER_NOT_FOUND:
                apiResponse.setCode(errorCode.USER_NOT_FOUND.getCode());
                apiResponse.setMessage(errorCode.USER_NOT_FOUND.getMessage());
                break;
            case POST_NOT_FOUND:
                apiResponse.setCode(errorCode.POST_NOT_FOUND.getCode());
                apiResponse.setMessage(errorCode.POST_NOT_FOUND.getMessage());
                break;
            default:
                apiResponse.setCode(1);
                apiResponse.setMessage("An unknown error has occurred.");
                break;
        }

        return ResponseEntity.badRequest().body(apiResponse);
    }

}
