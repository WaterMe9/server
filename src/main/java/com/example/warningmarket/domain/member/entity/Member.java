package com.example.warningmarket.domain.member.entity;

import com.example.warningmarket.common.value.DefaultValue;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private Double temperature = DefaultValue.DEFAULT_TEMPERATURE;
    private String profileImageUrl = DefaultValue.DEFAULT_PROFILE_URL;
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.USER;
    @Embedded
    private Address address;

    @Builder
    public Member(String username, String email, String password, Double temperature,
                  String profileImageUrl, Authority authority, Address address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.temperature = temperature;
        this.profileImageUrl = profileImageUrl;
        this.authority = authority;
        this.address = address;
    }
}
