package com.example.warningmarket.domain.member.exception;

import com.example.warningmarket.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_EXIST_MEMBER(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
