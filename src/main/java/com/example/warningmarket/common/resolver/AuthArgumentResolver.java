package com.example.warningmarket.common.resolver;

import com.example.warningmarket.common.annotation.Auth;
import com.example.warningmarket.common.exception.ApplicationException;
import com.example.warningmarket.common.util.SecurityUtil;
import com.example.warningmarket.domain.member.entity.Member;
import com.example.warningmarket.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.example.warningmarket.domain.member.exception.MemberErrorCode.NOT_EXIST_MEMBER;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(Auth.class);
        boolean isMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasAnnotation && isMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Long id = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(NOT_EXIST_MEMBER));

        return member;
    }
}
