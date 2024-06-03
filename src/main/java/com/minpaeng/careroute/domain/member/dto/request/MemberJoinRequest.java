package com.minpaeng.careroute.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class MemberJoinRequest {
    private String idToken;
    private String nickName;
    private String email;
    private String sns;
}
