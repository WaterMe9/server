package com.example.warningmarket.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 요청 값"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리소스"),
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "권한 없음"),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰 검증 실패"),
    MALFORMED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명"),
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰"),
    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
