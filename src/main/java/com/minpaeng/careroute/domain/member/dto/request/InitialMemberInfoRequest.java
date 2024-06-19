package com.minpaeng.careroute.domain.member.dto.request;

import lombok.Getter;

@Getter
public class InitialMemberInfoRequest {
    private String phoneNumber;
    private String nickname;
    private String authCode;
}
