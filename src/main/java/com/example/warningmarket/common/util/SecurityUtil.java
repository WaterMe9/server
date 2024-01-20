package com.example.warningmarket.common.util;

import com.example.warningmarket.common.exception.ApplicationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.warningmarket.common.exception.CommonErrorCode.UNAUTHORIZED_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityUtil {

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new ApplicationException(UNAUTHORIZED_EXCEPTION);
        }

        return Long.parseLong(authentication.getName());
    }
}
