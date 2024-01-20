package com.example.warningmarket.domain.member.exception;

import com.example.warningmarket.common.exception.ApplicationException;
import com.example.warningmarket.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.warningmarket.domain.member.exception.MemberErrorCode.*;

@Component
@RequiredArgsConstructor
public class MemberValidation {

    private final MemberRepository memberRepository;

    public void duplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new ApplicationException(DUPLICATE_EMAIL);
        }
    }

    public void checkPassword(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)) {
            throw new ApplicationException(NOT_MATCH_PASSWORD);
        }
    }
}
