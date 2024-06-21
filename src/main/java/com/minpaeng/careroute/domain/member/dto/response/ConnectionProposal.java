package com.minpaeng.careroute.domain.member.dto.response;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import lombok.Getter;

@Getter
public class ConnectionProposal {
    private int memberId;
    private String nickname;
    private String profileImage;
    private MemberRole role;

    private ConnectionProposal(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImagePath();
        this.role = member.getRole();
    }
}
