package com.minpaeng.careroute.domain.member.repository.entity.enums;

public enum SocialType {
    KAKAO("카카오"),
    GOOGLE("구글");

    private final String value;

    SocialType(String value) {
        this.value = value;
    }
}
