package com.example.warningmarket.domain.member.service;

import com.example.warningmarket.common.jwt.TokenProvider;
import com.example.warningmarket.common.service.SecurityService;
import com.example.warningmarket.domain.member.dto.MemberResponse;
import com.example.warningmarket.domain.member.dto.SignInRequest;
import com.example.warningmarket.domain.member.dto.SignUpRequest;
import com.example.warningmarket.domain.member.dto.TokenDto;
import com.example.warningmarket.domain.member.entity.Address;
import com.example.warningmarket.domain.member.entity.Member;
import com.example.warningmarket.domain.member.exception.MemberValidation;
import com.example.warningmarket.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidation memberValidation;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberResponse getMember(Member member) {
        return MemberResponse.builder().member(member).build();
    }

    @Transactional
    public void signUp(SignUpRequest request) {
        memberValidation.duplicateEmail(request.getEmail());
        memberValidation.checkPassword(request.getPassword(), request.getPasswordCheck());

        Address address = Address.builder()
                .city(request.getCity())
                .district(request.getDistrict())
                .streetNumber(request.getStreetNumber())
                .build();
        Member member = Member.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(address)
                .build();

        memberRepository.save(member);
    }

    public TokenDto signIn(SignInRequest request) {
        Authentication authentication = securityService.createAuthentication(request.getEmail(), request.getPassword());

        return tokenProvider.createTokenDto(authentication);
    }

    public TokenDto reissue(HttpServletRequest request, String refreshToken) {
        tokenProvider.validateToken(refreshToken);

        String accessToken = tokenProvider.resolveToken(request);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        return tokenProvider.createTokenDto(authentication);
    }
}
