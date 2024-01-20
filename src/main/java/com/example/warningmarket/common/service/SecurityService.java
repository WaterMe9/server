package com.example.warningmarket.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Authentication createAuthentication(String principal, String credentials) {
        //Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principal, credentials);

        //실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //authenticate 메서드가 실행이 될 때 CustomUserDetailsService 의 loadUserByUsername 메서드가 실행됨
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }
}
