package com.example.warningmarket.common.value;

public interface DefaultValue {

    // member
    Double DEFAULT_TEMPERATURE = 36.5;
    String DEFAULT_PROFILE_URL = "https://default";

    //jwt
    long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    String AUTHORIZATION_HEADER = "Authorization";
    String AUTHORITIES_KEY = "auth";
    String BEARER_PREFIX = "Bearer ";

    //cookie
    String COOKIE_NAME = "RefreshToken";
    long COOKIE_EXPIRE = 60 * 60 * 24 * 7;
}
