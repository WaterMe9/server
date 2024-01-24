package com.example.warningmarket.domain.item.exception;

import com.example.warningmarket.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemErrorCode implements ErrorCode {
    NOT_EXIST_IMAGE(HttpStatus.BAD_REQUEST, "한 개 이상의 상품 이미지가 필요합니다"),
    NOT_EXIST_ITEM(HttpStatus.BAD_REQUEST, "해당 상품이 존재하지 않습니다");


    private final HttpStatus httpStatus;
    private final String message;
}
