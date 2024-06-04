package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.ConnectionRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.repository.ConnectionRepository;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Connection;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.member.repository.entity.enums.SocialType;
import com.minpaeng.careroute.domain.member.security.OIDCDecodePayload;
import com.minpaeng.careroute.domain.member.security.OauthOIDCHelper;
import com.minpaeng.careroute.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final OauthOIDCHelper oauthOIDCHelper;
    private final MemberRepository memberRepository;
    private final ConnectionRepository connectionRepository;

    @Transactional
    @Override
    public MemberJoinResponse login(MemberJoinRequest memberJoinRequest) {
        String idToken = memberJoinRequest.getIdToken();
        log.info("서비스 진입: " + idToken);
        OIDCDecodePayload oidcDecodePayload = oauthOIDCHelper.getOIDCDecodePayload(idToken);
        Optional<Member> optionalMember = memberRepository.findMemberBySocialId(oidcDecodePayload.getSub());
        Member member;
        if (optionalMember.isEmpty()) {
            member = Member.builder()
                    .socialType(oidcDecodePayload.getIss().contains("kakao") ? SocialType.KAKAO : SocialType.GOOGLE)
                    .socialId(oidcDecodePayload.getSub())
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

    @Transactional
    @Override
    public BaseResponse connectDevice(String socialId, ConnectionRequest request) {
        Member from = memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다: from"));
        Member to = memberRepository.findMemberByPhoneNumber(request.getToNumber())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다: to"));

        Connection connection = Connection.builder()
                .from(from)
                .to(to)
                .authCode(request.getAuthCode())
                .build();

        connectionRepository.save(connection);
        return BaseResponse.builder()
                .statusCode(200)
                .message("기기연결 정보 저장 완료")
                .build();
    }
}
