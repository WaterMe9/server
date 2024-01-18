package com.example.warningmarket.domain.test.controller;

import com.example.warningmarket.common.exception.ApplicationException;
import com.example.warningmarket.common.exception.CommonErrorCode;
import com.example.warningmarket.common.exception.ErrorCode;
import com.example.warningmarket.common.response.ApplicationResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping("restdocs")
@RestController
public class TestController {

    @GetMapping("get-test")
    public TestResponse getTest() {
        return TestResponse.builder().name("란란루").build();
    }

    @PostMapping("post-test")
    public TestResponse postTest(@RequestBody TestRequest request) {
        return TestResponse.builder().name(request.getName()).build();
    }

    @GetMapping("response")
    public ApplicationResponse<?> response() {
        return new ApplicationResponse<>();
    }

    @GetMapping("response-data")
    public ApplicationResponse<TestResponse> responseData() {
        return new ApplicationResponse<>(TestResponse.builder().name("aaa").build());
    }

    @GetMapping("response-error")
    public ApplicationResponse<ErrorCode> responseError() {
        throw new ApplicationException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}
