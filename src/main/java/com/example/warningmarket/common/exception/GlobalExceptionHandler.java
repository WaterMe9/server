package com.example.warningmarket.common.exception;

import com.example.warningmarket.common.response.ApplicationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationResponse<ErrorCode>> applicationException(ApplicationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new ApplicationResponse<>(e.getErrorCode()));
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ApplicationResponse<ErrorCode>> runtimeException(RuntimeException e) {
//        CommonErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
//        return ResponseEntity
//                .status(errorCode.getHttpStatus())
//                .body(new ApplicationResponse<>(
//                        errorCode.getHttpStatus(),
//                        e.getMessage()
//                ));
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationResponse<ErrorCode>> validException(MethodArgumentNotValidException e) {
        CommonErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ApplicationResponse<>(
                        errorCode.getHttpStatus(),
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                ));
    }
}
