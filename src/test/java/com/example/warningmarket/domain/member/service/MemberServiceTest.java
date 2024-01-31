package com.example.warningmarket.domain.member.service;

import com.example.warningmarket.common.exception.ApplicationException;
import com.example.warningmarket.common.jwt.TokenProvider;
import com.example.warningmarket.common.service.SecurityService;
import com.example.warningmarket.domain.member.dto.MemberResponse;
import com.example.warningmarket.domain.member.dto.SignUpRequest;
import com.example.warningmarket.domain.member.entity.Address;
import com.example.warningmarket.domain.member.entity.Member;
import com.example.warningmarket.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SecurityService securityService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;
    @InjectMocks
    private MemberService memberService;

    public SignUpRequest setUpSignUpRequest(String password) {
        return SignUpRequest.builder()
                .username("윤문김")
                .email("aaa123")
                .password(password)
                .passwordCheck("pass123")
                .city("경기도")
                .district("파주시")
                .streetNumber("야당로")
                .build();
    }

    public Member setUpMember() {
        return Member.builder()
                .username("윤문김")
                .email("aaa123")
                .password("pass123")
                .address(
                        Address.builder()
                                .city("경기도")
                                .district("파주시")
                                .streetNumber("야당로")
                                .build()
                )
                .build();
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCase {
        @Test
        @DisplayName("회원가입 테스트")
        void signUpTest() {
            //given
            SignUpRequest request = setUpSignUpRequest("pass123");
            when(memberRepository.save(any())).thenReturn(request.toEntity());

            //when
            memberService.signUp(request);

            //then
        }

        @Test
        @DisplayName("회원 조회 테스트")
        void getMemberTest() {
            //given
            Member member = setUpMember();

            //when
            MemberResponse response = memberService.getMember(member);

            //then
            assertThat(response.getEmail()).isEqualTo(member.getEmail());
            assertThat(response.getCity()).isEqualTo(member.getAddress().getCity());
        }
    }

    @Nested
    @DisplayName("비정상 테스트")
    class FailCase {
        @Test
        @DisplayName("회원가입 - 중복 이메일")
        void signUpException1() {
            //given
            SignUpRequest request = setUpSignUpRequest("pass123");
            when(memberRepository.existsByEmail(any())).thenReturn(true);

            //when
            Throwable throwable = catchThrowable(() -> memberService.signUp(request));

            //then
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class);
        }

        @Test
        @DisplayName("회원가입 - 비밀번호 불일치")
        void signUpException2() {
            //given
            SignUpRequest request = setUpSignUpRequest("pass12345");

            //when
            Throwable throwable = catchThrowable(() -> memberService.signUp(request));

            //then
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class);
        }
    }
}