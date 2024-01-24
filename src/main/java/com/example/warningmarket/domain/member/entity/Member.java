package com.example.warningmarket.domain.member.entity;

import com.example.warningmarket.common.value.DefaultValue;
import com.example.warningmarket.domain.item.entity.Love;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "member")
    private List<Love> loves = new ArrayList<>();

    @Builder
    public Member(String username, String email, String password,
                  String profileImageUrl, Authority authority, Address address) {
        this.username = username;
        this.email = email;
        this.password = password;
        if(profileImageUrl != null) this.profileImageUrl = profileImageUrl;
        if(authority != null) this.authority = authority;
        this.address = address;
    }

    public void updateTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
