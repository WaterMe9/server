package com.example.warningmarket.domain.member.controller;

import com.example.warningmarket.common.AbstractRestDocsTests;
import com.example.warningmarket.common.jwt.TokenProvider;
import com.example.warningmarket.common.value.DefaultValue;
import com.example.warningmarket.domain.member.dto.SignInRequest;
import com.example.warningmarket.domain.member.dto.SignUpRequest;
import com.example.warningmarket.domain.member.dto.TokenDto;
import com.example.warningmarket.domain.member.repository.MemberRepository;
import com.example.warningmarket.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends AbstractRestDocsTests {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TokenProvider tokenProvider;

    public SignUpRequest setUpSignUpRequest() {
        return SignUpRequest.builder()
                .username("윤문김")
                .email("aaa123")
                .password("pass123")
                .passwordCheck("pass123")
                .city("경기도")
                .district("파주시")
                .streetNumber("야당로")
                .build();
    }

    public SignInRequest setUpSignInRequest() {
        return SignInRequest.builder()
                .email("aaa123")
                .password("pass123")
                .build();
    }

    public void signUp() {
        SignUpRequest request = setUpSignUpRequest();
        memberService.signUp(request);
    }

    public TokenDto signIn() {
        SignInRequest request = setUpSignInRequest();
        return memberService.signIn(request);
    }

    @Test
    @DisplayName("사용자 정보 조회 테스트")
    void getMemberTest() throws Exception {
        signUp();
        String accessToken = signIn().getAccessToken();

        mockMvc.perform(
                        get("/api/member")
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                        document(
                                "member-controller-test/사용자_정보_조회_테스트",
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("username").description("이름"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("temperature").description("온도"),
                                        fieldWithPath("profile_image_url").description("프로필 이미지 URL"),
                                        fieldWithPath("city").description("주소(도)"),
                                        fieldWithPath("district").description("주소(시)"),
                                        fieldWithPath("street_number").description("주소(도로명 주소)")
                                )
                        )
                );
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws Exception {
        SignUpRequest request = setUpSignUpRequest();
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/api/member/sign-up")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "member-controller-test/회원가입_테스트",
                                requestFields(
                                        fieldWithPath("username").description("이름"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("비밀번호"),
                                        fieldWithPath("password_check").description("비밀번호 확인"),
                                        fieldWithPath("city").description("주소(도)"),
                                        fieldWithPath("district").description("주소(시)"),
                                        fieldWithPath("street_number").description("주소(도로명 주소)")
                                ),
                                responseFields(
                                        fieldWithPath("status").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                );
    }

    @Test
    @DisplayName("로그인 테스트")
    void signInTest() throws Exception {
        signUp();
        SignInRequest request = setUpSignInRequest();
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/api/member/sign-in")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "member-controller-test/로그인_테스트",
                                requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("비밀번호")
                                ),
                                responseFields(
                                        fieldWithPath("status").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data").description("액세스 토큰")
                                )
                        )
                );
    }

    @Test
    @DisplayName("토큰 재발급 테스트")
    void reissueTest() throws Exception {
        signUp();
        TokenDto tokenDto = signIn();
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        Cookie cookie = new Cookie(DefaultValue.COOKIE_NAME, refreshToken);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge((int) DefaultValue.COOKIE_EXPIRE);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        mockMvc.perform(
                        post("/api/member/reissue")
                        .header("Authorization", accessToken)
                        .cookie(cookie)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "member-controller-test/토큰_재발급_테스트",
                                responseFields(
                                        fieldWithPath("status").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data").description("액세스 토큰")
                                )
                        )
                );
    }
}