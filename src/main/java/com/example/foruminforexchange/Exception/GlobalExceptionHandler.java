package com.example.foruminforexchange.Exception;

import com.example.foruminforexchange.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
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
            case NOT_ADMIN:
                apiResponse.setCode(errorCode.NOT_ADMIN.getCode());
                apiResponse.setMessage(errorCode.NOT_ADMIN.getMessage());
                break;
            case NOT_FOUND_TOPIC:
                apiResponse.setCode(errorCode.NOT_FOUND_TOPIC.getCode());
                apiResponse.setMessage(errorCode.NOT_FOUND_TOPIC.getMessage());
                break;
            case NOT_BLANK:
                apiResponse.setCode(errorCode.NOT_BLANK.getCode());
                apiResponse.setMessage(errorCode.NOT_BLANK.getMessage());
                break;
            case PASSWORD_NOT_TRUE:
                apiResponse.setCode(errorCode.PASSWORD_NOT_TRUE.getCode());
                apiResponse.setMessage(errorCode.PASSWORD_NOT_TRUE.getMessage());
                break;
            case LOCKED_USER:
                apiResponse.setCode(errorCode.LOCKED_USER.getCode());
                apiResponse.setMessage(errorCode.LOCKED_USER.getMessage());
                break;
            case REPORT_EXISTED:
                apiResponse.setCode(errorCode.REPORT_EXISTED.getCode());
                apiResponse.setMessage(errorCode.REPORT_EXISTED.getMessage());
                break;
            case NOT_ENOUGH_AUTHORITY:
                apiResponse.setCode(errorCode.NOT_ENOUGH_AUTHORITY.getCode());
                apiResponse.setMessage(errorCode.NOT_ENOUGH_AUTHORITY.getMessage());
                break;
            case DELETE_CONSTRAINT_VIOLATION:
                apiResponse.setCode(errorCode.DELETE_CONSTRAINT_VIOLATION.getCode());
                apiResponse.setMessage(errorCode.DELETE_CONSTRAINT_VIOLATION.getMessage());
                break;
            default:
                apiResponse.setCode(1);
                apiResponse.setMessage("An unknown error has occurred.");
                break;
        }

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException exception) {
        ApiResponse response = new ApiResponse();
        response.setCode(HttpStatus.UNAUTHORIZED.value());
        response.setMessage("Phiên làm việc đã kết thúc. Vui lòng đăng nhập lại.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
