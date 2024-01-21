package com.example.warningmarket.domain.item.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemErrorCode {


    private final HttpStatus httpStatus;
    private final String message;
}
