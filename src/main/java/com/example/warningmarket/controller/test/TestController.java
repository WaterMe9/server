package com.example.warningmarket.controller.test;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("restdocs")
@RestController
public class TestController {

    @GetMapping("test")
    public String test() {
        return "test";
    }

    @GetMapping("get-test")
    public TestResponse getTest() {
        return TestResponse.builder().name("란란루").build();
    }

    @PostMapping("post-test")
    public TestResponse postTest(@RequestBody TestRequest request) {
        return TestResponse.builder().name(request.getName()).build();
    }

}
