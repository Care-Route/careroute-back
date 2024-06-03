package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import com.minpaeng.careroute.domain.member.repository.entity.enums.SocialType;
import com.minpaeng.careroute.domain.member.security.KakaoOauthHelper;
import com.minpaeng.careroute.domain.member.security.OIDCDecodePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final KakaoOauthHelper kakaoOauthHelper;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public MemberJoinResponse login(MemberJoinRequest memberJoinRequest) {
        String idToken = memberJoinRequest.getIdToken();
        System.out.println("서비스 진입: " + idToken);
        OIDCDecodePayload oidcDecodePayload = kakaoOauthHelper.getOIDCDecodePayload(idToken);
        Optional<Member> optionalMember = memberRepository.findMemberBySocialId(oidcDecodePayload.getSub());
        Member member;
        if (optionalMember.isEmpty()) {
            member = Member.builder()
                    .socialType(oidcDecodePayload.getIss().contains("kakao") ? SocialType.KAKAO : SocialType.GOOGLE)
                    .socialId(oidcDecodePayload.getSub())
                    .role(memberJoinRequest.getUserType().equals(
                            MemberRole.GUIDE.getValue()) ? MemberRole.GUIDE : MemberRole.TARGET)
                    .nickname(memberJoinRequest.getNickName())
                    .build();
            memberRepository.save(member);
        } else member = optionalMember.get();

        return MemberJoinResponse.builder()
                .statusCode(200)
                .message("로그인 완료")
                .userId(member.getId())
                .build();
    }

    private void loginByKakao(MemberJoinRequest memberJoinRequest) {

    }
}
