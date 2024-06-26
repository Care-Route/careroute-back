package com.minpaeng.careroute.domain.member.dto.response;

import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
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
    private int memberId;
    private String nickname;
    private MemberRole type;
    private String phoneNumber;
    private String imageUrl;
}
