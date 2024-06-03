package com.minpaeng.careroute.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberJoinResponse {
    private int statusCode;
    private String message;
    private int userId;
}
