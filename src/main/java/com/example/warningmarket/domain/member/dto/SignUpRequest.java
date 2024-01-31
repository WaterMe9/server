package com.example.warningmarket.domain.member.dto;

import com.example.warningmarket.domain.member.entity.Address;
import com.example.warningmarket.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {

    @NotBlank(message = "이름은 필수값입니다.")
    private String username;

    @NotBlank(message = "이메일은 필수값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;

    @JsonProperty("password_check")
    @NotBlank(message = "비밀번호 확인은 필수값입니다.")
    private String passwordCheck;

    @NotBlank(message = "주소(도)는 필수값입니다.")
    private String city;

    @NotBlank(message = "주소(시)는 필수값입니다.")
    private String district;

    @JsonProperty("street_number")
    @NotBlank(message = "주소(도로명 주소)는 필수값입니다.")
    private String streetNumber;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .email(email)
                .address(
                        Address.builder()
                                .city(city)
                                .district(district)
                                .streetNumber(streetNumber)
                                .build()
                )
                .build();
    }
}
