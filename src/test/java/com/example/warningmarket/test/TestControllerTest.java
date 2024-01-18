package com.example.warningmarket.test;

import com.example.warningmarket.common.AbstractRestDocsTests;
import com.example.warningmarket.domain.test.controller.TestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestControllerTest extends AbstractRestDocsTests {


    @Test
    public void GET_테스트() throws Exception {
        mockMvc.perform(get("/restdocs/get-test"))
                .andExpect(status().isOk());
    }

    @Test
    public void POST_테스트() throws Exception {
        TestRequest request = TestRequest.builder().id(1L).name("란란루").build();
        mockMvc.perform(post("/restdocs/post-test")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("test-controller-test/post_테스트",
                                requestFields(
                                        fieldWithPath("id").description("사용자아이디"),
                                        fieldWithPath("name").description("사용자이름")
                                ),
                                responseFields(
                                        fieldWithPath("name").description("사용자이름")
                                )
                        )
                );
    }



}
