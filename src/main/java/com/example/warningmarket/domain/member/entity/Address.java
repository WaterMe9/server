package com.example.warningmarket.domain.member.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String district;
    private String streetNumber;

    @Builder
    public Address(String city, String district, String streetNumber) {
        this.city = city;
        this.district = district;
        this.streetNumber = streetNumber;
    }
}
