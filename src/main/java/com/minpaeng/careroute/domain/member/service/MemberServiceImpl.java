package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.ConnectionRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.repository.ConnectionRepository;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Connection;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import com.minpaeng.careroute.domain.member.repository.entity.enums.SocialType;
import com.minpaeng.careroute.domain.member.security.OIDCDecodePayload;
import com.minpaeng.careroute.domain.member.security.OauthOIDCHelper;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        boolean newMember = false;
        if (optionalMember.isEmpty()) {
            newMember = true;
            member = Member.builder()
                    .socialType(oidcDecodePayload.getIss().contains("kakao") ? SocialType.KAKAO : SocialType.GOOGLE)
                    .socialId(oidcDecodePayload.getSub())
                    .nickname(memberJoinRequest.getNickname())
                    .build();
            memberRepository.save(member);
        } else member = optionalMember.get();

        return MemberJoinResponse.builder()
                .statusCode(200)
                .message("로그인 완료")
                .memberId(member.getId())
                .newMember(newMember)
                .build();
    }

    @Transactional
    @Override
    public BaseResponse selectType(String idToken, String socialId, String type) {
        if (!type.equals(MemberRole.GUIDE.name()) && !type.equals(MemberRole.TARGET.name())) {
            throw CustomException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(400)
                    .message("잘못된 권한: GUIDE 또는 TARGET만 유효합니다.")
                    .build();
        }

        Member member = memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> new IllegalStateException("해당하는 사용자가 존재하지 않습니다."));
        if (member.getRole() != null) throw CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("이미 사용자 유형이 선택되었습니다: " + member.getRole().name())
                .build();
        log.info(member.toString());
        member.setRole(MemberRole.valueOf(type));

        oauthOIDCHelper.setAuthentication(idToken, socialId);
        return BaseResponse.builder()
                .statusCode(200)
                .message("사용자 권한이 업데이트 되었습니다: " + type)
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
