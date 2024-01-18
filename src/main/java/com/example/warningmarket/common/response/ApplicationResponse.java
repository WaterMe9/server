package com.example.warningmarket.common.response;

import com.example.warningmarket.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ApplicationResponse<T> {

    private int status = HttpStatus.OK.value();
    private String message = "성공";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApplicationResponse(T data) {
        this.data = data;
    }

    public ApplicationResponse(ErrorCode errorCode) {
        this.status = errorCode.getHttpStatus().value();
        this.message = errorCode.getMessage();
    }

    public ApplicationResponse(HttpStatus httpStatus, String message) {
        this.status = httpStatus.value();
        this.message = message;
    }
}
