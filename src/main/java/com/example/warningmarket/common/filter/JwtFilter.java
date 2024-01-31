package com.example.warningmarket.common.filter;

import com.example.warningmarket.common.exception.ApplicationException;
import com.example.warningmarket.common.jwt.TokenProvider;
import com.example.warningmarket.common.response.ApplicationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //유효성 검사 후 토큰에 해당하는 Authentication 을 가져와 SecurityContext 에 저장
        try {
            String jwt = tokenProvider.resolveToken(request);
            tokenProvider.validateToken(jwt);
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ApplicationException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), new ApplicationResponse<>(e.getErrorCode()));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] excludePath = {
                "/", "/error/**",
                "/health/check", "/restdocs/**",
                "/api/member/sign-up", "/api/member/sign-in"
        };
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(ep -> pathMatcher.match(ep, path));
    }
}
