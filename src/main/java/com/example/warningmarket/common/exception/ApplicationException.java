package com.example.warningmarket.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
}
