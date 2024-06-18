package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.ConnectionDto;
import com.minpaeng.careroute.domain.member.dto.request.ConnectionCodeRequest;
import com.minpaeng.careroute.domain.member.dto.request.ConnectionRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.repository.ConnectionRepository;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Connection;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import com.minpaeng.careroute.domain.member.repository.entity.enums.SocialType;
import com.minpaeng.careroute.domain.member.repository.redis.ConnectionAuthRepository;
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
    private final ConnectionAuthRepository connectionAuthRepository;

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
                    .nickname(memberJoinRequest.getNickname())
                    .build();
            memberRepository.save(member);
        } else member = optionalMember.get();

        return MemberJoinResponse.builder()
                .statusCode(200)
                .message("로그인 완료")
                .memberId(member.getId())
                .type(member.getRole())
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

        Member member = getMemger(socialId);
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

    @Override
    public BaseResponse connectCode(String socialId, ConnectionCodeRequest request) {
        Member member1 = getMemger(socialId);
        Member member2 = memberRepository.findMemberByPhoneNumber(request.getToNumber())
                .orElseThrow(() -> CustomException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("연락처 " + request.getToNumber() + "에 해당하는 사용자를 찾을 수 없습니다.")
                        .build());

//        if (member1.getRole() == member2.getRole())  {
//            throw CustomException.builder()
//                    .status(HttpStatus.BAD_REQUEST)
//                    .code(HttpStatus.NO_CONTENT.value())
//                    .message("유효하지 않은 유저 유형에 대한 요청입니다.")
//                    .build();
//        }

        ConnectionDto connectionDto = ConnectionDto.builder()
                .fromNumber(member1.getPhoneNumber())
                .toNumber(member2.getPhoneNumber())
                .code(request.getAuthCode())
                .build();

        connectionAuthRepository.save(connectionDto);
        return BaseResponse.builder()
                .statusCode(200)
                .message("기기 연결을 위한 인증 정보 생성 완료: 10분 유지")
                .build();
    }

    @Transactional
    @Override
    public BaseResponse connectDevice(String socialId, ConnectionRequest request) {
        Member to = memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다: from"));
        Member from = memberRepository.findMemberByPhoneNumber(request.getFromNumber())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다: to"));
        ConnectionDto connectionDto = connectionAuthRepository
                .findByFromNumberAndToNumber(from.getPhoneNumber(), to.getPhoneNumber())
                .orElseThrow(() -> CustomException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("인증 정보가 존재하지 않습니다.")
                        .build());

        if (!connectionDto.getCode().equals(request.getAuthCode())) {
            throw CustomException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("인증 정보가 일치하지 않습니다. 등록 인증번호: " + connectionDto.getCode()
                            + ", 요청 인증번호: " + request.getAuthCode())
                    .build();
        }

        Connection connection;
        if (to.getRole() == MemberRole.GUIDE && from.getRole() == MemberRole.TARGET) {
             connection = Connection.builder()
                    .guide(to)
                    .target(from)
                    .build();
        } else if (to.getRole() == MemberRole.TARGET && from.getRole() == MemberRole.GUIDE) {
            connection = Connection.builder()
                    .guide(from)
                    .target(to)
                    .build();
        }
        else {
//            throw CustomException.builder()
//                    .status(HttpStatus.BAD_REQUEST)
//                    .code(HttpStatus.NO_CONTENT.value())
//                    .message("유효하지 않은 유저 유형에 대한 요청입니다: "
//                            + to.getRole() + "-" + from.getRole() + "연결 시도 중")
//                    .build();
            connection = Connection.builder()
                    .guide(from)
                    .target(to)
                    .build();
        }
        connectionAuthRepository.delete(connectionDto);
        connectionRepository.save(connection);
        return BaseResponse.builder()
                .statusCode(200)
                .message("연결 생성 완료")
                .build();
    }

    private Member getMemger(String socialId) {
        return memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> new IllegalStateException("해당하는 사용자가 존재하지 않습니다."));
    }
}
