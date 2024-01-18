package com.example.warningmarket.domain.member.controller;

import com.example.warningmarket.common.exception.ApplicationException;
import com.example.warningmarket.common.response.ApplicationResponse;
import com.example.warningmarket.domain.member.dto.SignUpRequest;
import com.example.warningmarket.domain.member.exception.MemberErrorCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @PostMapping("/sign-up")
    public ApplicationResponse<?> signUp(@Validated @RequestBody SignUpRequest request) {
        return new ApplicationResponse<>();
    }

    @GetMapping("/error")
    public void error() {
        throw new ApplicationException(MemberErrorCode.NOT_MATCH_PASSWORD);
    }
}
