package com.example.warningmarket.domain.member.controller;

import com.example.warningmarket.common.annotation.Auth;
import com.example.warningmarket.common.response.ApplicationResponse;
import com.example.warningmarket.domain.member.dto.MemberResponse;
import com.example.warningmarket.domain.member.dto.SignInRequest;
import com.example.warningmarket.domain.member.dto.SignUpRequest;
import com.example.warningmarket.domain.member.dto.TokenDto;
import com.example.warningmarket.domain.member.entity.Member;
import com.example.warningmarket.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.warningmarket.common.value.DefaultValue.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<ApplicationResponse<MemberResponse>> getMember(@Auth Member member) {
        return ResponseEntity.ok(new ApplicationResponse<>(memberService.getMember(member)));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApplicationResponse<?>> signUp(@Validated @RequestBody SignUpRequest request) {
        memberService.signUp(request);
        return ResponseEntity.ok(new ApplicationResponse<>());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApplicationResponse<String>> signIn(@Validated @RequestBody SignInRequest request,
                                                              HttpServletRequest httpServletRequest,
                                                              HttpServletResponse httpServletResponse) {
        TokenDto tokenDto = memberService.signIn(request);
        setRefreshToken(httpServletRequest, httpServletResponse, tokenDto.getRefreshToken());

        return ResponseEntity.ok(new ApplicationResponse<>(tokenDto.getAccessToken()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApplicationResponse<String>> reissue(@CookieValue(name = "RefreshToken", required = false, defaultValue = "") String refreshToken,
                                                               HttpServletRequest httpServletRequest,
                                                               HttpServletResponse httpServletResponse) {
        TokenDto tokenDto = memberService.reissue(httpServletRequest, refreshToken);
        setRefreshToken(httpServletRequest, httpServletResponse, tokenDto.getRefreshToken());

        return ResponseEntity.ok(new ApplicationResponse<>(tokenDto.getAccessToken()));
    }

    private void setRefreshToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, refreshToken)
                .domain(request.getServerName())
                .maxAge(COOKIE_EXPIRE)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }
}
