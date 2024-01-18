package com.example.warningmarket.domain.member.exception;

import com.example.warningmarket.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 불일치"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
