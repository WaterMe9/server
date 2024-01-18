package com.example.warningmarket.domain.test.controller;

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

}
