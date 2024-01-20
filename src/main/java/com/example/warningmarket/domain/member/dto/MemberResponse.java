package com.example.warningmarket.domain.member.dto;

import com.example.warningmarket.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private String username;
    private String email;
    private Double temperature;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    private String city;
    private String district;
    @JsonProperty("street_number")
    private String streetNumber;

    @Builder
    public MemberResponse(Member member) {
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.temperature = member.getTemperature();
        this.profileImageUrl = member.getProfileImageUrl();
        this.city = member.getAddress().getCity();
        this.district = member.getAddress().getDistrict();
        this.streetNumber = member.getAddress().getStreetNumber();
    }
}
