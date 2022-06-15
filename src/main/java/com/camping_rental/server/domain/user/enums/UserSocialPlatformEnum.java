package com.camping_rental.server.domain.user.enums;

public enum UserSocialPlatformEnum {
    NAVER("naver"),
    KAKAO("kakao"),
    GOOGLE("google"),
    FACEBOOK("facebook"),
    GITHUB("github")
    ;

    private String value;


    UserSocialPlatformEnum(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
