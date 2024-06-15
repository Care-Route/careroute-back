package com.minpaeng.careroute.domain.member.dto.request;

import lombok.Getter;

@Getter
public class ConnectionCodeRequest {
    private String toNumber;
    private String authCode;
}
