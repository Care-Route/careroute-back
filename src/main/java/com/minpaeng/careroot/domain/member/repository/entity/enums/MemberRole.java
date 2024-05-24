package com.minpaeng.careroot.domain.member.repository.entity.enums;

import lombok.Getter;

@Getter
public enum MemberRole {

    TEACHER("안내인"),
    STUDENT("안내대상");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }
}