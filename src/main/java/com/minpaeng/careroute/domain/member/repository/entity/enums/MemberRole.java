package com.minpaeng.careroute.domain.member.repository.entity.enums;

import lombok.Getter;

@Getter
public enum MemberRole {

    GUIDE("guide"),
    TARGET("target");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }
}