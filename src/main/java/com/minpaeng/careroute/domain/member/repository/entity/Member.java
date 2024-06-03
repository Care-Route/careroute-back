package com.minpaeng.careroute.domain.member.repository.entity;

import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import com.minpaeng.careroute.domain.member.repository.entity.enums.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false, length = 200, unique = true)
    private String socialId;

    @Column(length = 200)
    private String nickname;

    @Column(length = 3000)
    private String address;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 400)
    private String profileImagePath;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(SocialType socialType, String socialId, String nickname, MemberRole role) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.nickname = nickname;
        this.role = role;
    }
}
