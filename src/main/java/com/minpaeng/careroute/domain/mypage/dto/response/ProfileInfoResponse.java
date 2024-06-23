package com.minpaeng.careroute.domain.mypage.dto.response;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import lombok.Getter;

@Getter
public class ProfileInfoResponse {
    private final Integer memberId;
    private final String nickname;
    private final MemberRole role;
    private final String address;
    private final String profileImage;
    private final String phoneNumber;

    public ProfileInfoResponse(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.role = member.getRole();
        this.address = member.getAddress();
        this.profileImage = member.getProfileImagePath();
        this.phoneNumber = member.getPhoneNumber();
    }
}
