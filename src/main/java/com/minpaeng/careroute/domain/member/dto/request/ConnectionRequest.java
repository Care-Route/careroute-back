package com.minpaeng.careroute.domain.member.dto.request;

import lombok.Getter;

@Getter
public class ConnectionRequest {
    private String fromNumber;
    private String authCode;
}
