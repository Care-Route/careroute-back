package com.minpaeng.careroute.domain.routine.dto.response;

import com.minpaeng.careroute.domain.member.repository.entity.Connection;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import lombok.Getter;

@Getter
public class TargetInfoResponse {
    private int memberId;
    private String nickname;
    private String profileImage;

    public TargetInfoResponse(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImagePath();
    }
}
