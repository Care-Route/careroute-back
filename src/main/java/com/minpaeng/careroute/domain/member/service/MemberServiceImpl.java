package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.ConnectionDto;
import com.minpaeng.careroute.domain.member.dto.PhoneAuthDto;
import com.minpaeng.careroute.domain.member.dto.request.ConnectionProposalRequest;
import com.minpaeng.careroute.domain.member.dto.request.InitialMemberInfoRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.ConnectionProposalResponse;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.dto.response.MemberRoleResponse;
import com.minpaeng.careroute.domain.member.repository.ConnectionRepository;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Connection;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import com.minpaeng.careroute.domain.member.repository.entity.enums.SocialType;
import com.minpaeng.careroute.domain.member.repository.redis.ConnectionAuthRepository;
import com.minpaeng.careroute.domain.member.repository.redis.PhoneAuthRepository;
import com.minpaeng.careroute.domain.member.security.OIDCDecodePayload;
import com.minpaeng.careroute.domain.member.security.OauthOIDCHelper;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.event.SMSAuthEvent;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final OauthOIDCHelper oauthOIDCHelper;
    private final MemberRepository memberRepository;
    private final PhoneAuthRepository phoneAuthRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionAuthRepository connectionAuthRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final SimpMessagingTemplate template;

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
                .nickname(member.getNickname())
                .type(getType(member))
                .phoneNumber(member.getPhoneNumber())
                .imageUrl(member.getProfileImagePath())
                .build();
    }

    @Override
    @Transactional
    public BaseResponse sendAuthCode(String socialId, String phoneNumber) {
        Member member = getMember(socialId);
        String randomNumber = createRandomNumber();
        PhoneAuthDto phoneAuthDto = PhoneAuthDto.builder()
                .memberId(member.getId())
                .phoneNumber(phoneNumber)
                .code(randomNumber)
                .build();
        if (phoneAuthRepository.existsByMemberId(member.getId()))
            phoneAuthRepository.deleteByMemberId(member.getId());
        phoneAuthRepository.save(phoneAuthDto);

        eventPublisher.publishEvent(new SMSAuthEvent(phoneNumber, randomNumber));
        return BaseResponse.builder()
                .statusCode(200)
                .message("인증 코드 발송 완료: : 3분 유지")
                .build();
    }

    @Override
    @Transactional
    public BaseResponse makeInitialInfo(String socialId, InitialMemberInfoRequest request) {
        log.info("이니셜 인포 요청: " + request);
        Member member = getMember(socialId);
        PhoneAuthDto phoneAuthDto = phoneAuthRepository.findByMemberId(member.getId())
                .orElseThrow(this::getNotExistPhoneAuthException);

        if (!phoneAuthDto.getCode().equals(request.getAuthCode()))
            throw getInvalidPhoneAuthCodeException(
                    phoneAuthDto.getCode(),
                    request.getAuthCode()
            );
        phoneAuthRepository.delete(phoneAuthDto);

        member.setInitialInfo(request.getPhoneNumber(), request.getNickname());
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("인증번호 일치: 전화번호 및 닉네임 설정 완료")
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

        Member member = getMember(socialId);
        if (getType(member) != null) throw CustomException.builder()
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
    @Transactional
    public BaseResponse makeConnectionProposal(String socialId, ConnectionProposalRequest request) {
        Member member1 = getMember(socialId);
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

        ConnectionDto connectionDto;
        if (member1.getRole() == MemberRole.GUIDE && member2.getRole() == MemberRole.TARGET) {
            connectionAuthRepository.deleteByGuideIdAndTargetId(member1.getId(), member2.getId());
            connectionDto = ConnectionDto.builder()
                    .guideId(member1.getId())
                    .targetId(member2.getId())
                    .build();
        } else if (member1.getRole() == MemberRole.TARGET && member2.getRole() == MemberRole.GUIDE) {
            connectionAuthRepository.deleteByGuideIdAndTargetId(member2.getId(), member1.getId());
            connectionDto = ConnectionDto.builder()
                    .guideId(member2.getId())
                    .targetId(member1.getId())
                    .build();
        } else {
            connectionAuthRepository.deleteByGuideIdAndTargetId(member1.getId(), member2.getId());
            connectionDto = ConnectionDto.builder()
                    .guideId(member1.getId())
                    .targetId(member2.getId())
                    .build();
        }

        connectionAuthRepository.save(connectionDto);
        log.info("기기 연결 요청 시작");
        Map<String, Object> headers = Map.of("success", true, "type", "connectionProposal");
        ConnectionProposalResponse response = ConnectionProposalResponse.builder()
                .member(member1)
                .build();
        template.convertAndSend("/sub/" + member1.getId(), response, headers);
        log.info("기기 연결 요청 종료: ");

        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("기기 연결 요청 성공: 10분 유지")
                .build();
    }

    @Override
    @Transactional
    public BaseResponse makeConnection(String socialId, int memberId) {
        Member to = getMember(socialId);
        Member from = getMember(memberId);

        ConnectionDto connectionDto;
        Connection connection;
        if (to.getRole() == MemberRole.GUIDE && from.getRole() == MemberRole.TARGET) {
            connectionDto = connectionAuthRepository
                    .findByGuideIdAndTargetId(to.getId(), from.getId())
                    .orElseThrow(() -> CustomException.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("인증 정보가 존재하지 않습니다.")
                            .build());
             connection = Connection.builder()
                    .guide(to)
                    .target(from)
                    .build();
        } else if (to.getRole() == MemberRole.TARGET && from.getRole() == MemberRole.GUIDE) {
            connectionDto = connectionAuthRepository
                    .findByGuideIdAndTargetId(from.getId(), to.getId())
                    .orElseThrow(() -> CustomException.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("인증 정보가 존재하지 않습니다.")
                            .build());
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
            connectionDto = connectionAuthRepository
                    .findByGuideIdAndTargetId(to.getId(), from.getId())
                    .orElseThrow(() -> CustomException.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("인증 정보가 존재하지 않습니다.")
                            .build());
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

    @Override
    @Transactional
    public BaseResponse deleteConnection(String socialId, int memberId) {
        Member member1 = getMember(socialId);
        Member member2 = getMember(memberId);
        if (member1.getRole() == MemberRole.GUIDE) {
            connectionRepository.deleteByGuideAndTarget(member1, member2);
        } else if (member1.getRole() == MemberRole.TARGET) {
            connectionRepository.deleteByGuideAndTarget(member2, member1);
        }
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("연결 삭제 완료")
                .build();
    }

    @Override
    public MemberRoleResponse getMemberRoleByPhoneNumber(String phoneNumber) {
        Member member = memberRepository.findMemberByPhoneNumber(phoneNumber)
                .orElseThrow(this::getNotExistMember);

        return new MemberRoleResponse(getType(member));
    }


//    @Override
//    public BaseResponse connectCode(String socialId, ConnectionCodeRequest request) {
//        Member member1 = getMemger(socialId);
//        Member member2 = memberRepository.findMemberByPhoneNumber(request.getToNumber())
//                .orElseThrow(() -> CustomException.builder()
//                        .status(HttpStatus.BAD_REQUEST)
//                        .code(HttpStatus.BAD_REQUEST.value())
//                        .message("연락처 " + request.getToNumber() + "에 해당하는 사용자를 찾을 수 없습니다.")
//                        .build());
//
////        if (member1.getRole() == member2.getRole())  {
////            throw CustomException.builder()
////                    .status(HttpStatus.BAD_REQUEST)
////                    .code(HttpStatus.NO_CONTENT.value())
////                    .message("유효하지 않은 유저 유형에 대한 요청입니다.")
////                    .build();
////        }
//
//        ConnectionDto connectionDto = ConnectionDto.builder()
//                .fromNumber(member1.getPhoneNumber())
//                .toNumber(member2.getPhoneNumber())
//                .code(request.getAuthCode())
//                .build();
//
//        connectionAuthRepository.save(connectionDto);
//        return BaseResponse.builder()
//                .statusCode(200)
//                .message("기기 연결을 위한 인증 정보 생성 완료: 10분 유지")
//                .build();
//    }

//    @Transactional
//    @Override
//    public BaseResponse connectDevice(String socialId, ConnectionRequest request) {
//        Member to = memberRepository.findMemberBySocialId(socialId)
//                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다: from"));
//        Member from = memberRepository.findMemberByPhoneNumber(request.getFromNumber())
//                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다: to"));
//        ConnectionDto connectionDto = connectionAuthRepository
//                .findByFromNumberAndToNumber(from.getPhoneNumber(), to.getPhoneNumber())
//                .orElseThrow(() -> CustomException.builder()
//                        .status(HttpStatus.BAD_REQUEST)
//                        .code(HttpStatus.BAD_REQUEST.value())
//                        .message("인증 정보가 존재하지 않습니다.")
//                        .build());
//
//        if (!connectionDto.getCode().equals(request.getAuthCode())) {
//            throw CustomException.builder()
//                    .status(HttpStatus.BAD_REQUEST)
//                    .code(HttpStatus.BAD_REQUEST.value())
//                    .message("인증 정보가 일치하지 않습니다. 등록 인증번호: " + connectionDto.getCode()
//                            + ", 요청 인증번호: " + request.getAuthCode())
//                    .build();
//        }
//
//        Connection connection;
//        if (to.getRole() == MemberRole.GUIDE && from.getRole() == MemberRole.TARGET) {
//             connection = Connection.builder()
//                    .guide(to)
//                    .target(from)
//                    .build();
//        } else if (to.getRole() == MemberRole.TARGET && from.getRole() == MemberRole.GUIDE) {
//            connection = Connection.builder()
//                    .guide(from)
//                    .target(to)
//                    .build();
//        }
//        else {
////            throw CustomException.builder()
////                    .status(HttpStatus.BAD_REQUEST)
////                    .code(HttpStatus.NO_CONTENT.value())
////                    .message("유효하지 않은 유저 유형에 대한 요청입니다: "
////                            + to.getRole() + "-" + from.getRole() + "연결 시도 중")
////                    .build();
//            connection = Connection.builder()
//                    .guide(from)
//                    .target(to)
//                    .build();
//        }
//        connectionAuthRepository.delete(connectionDto);
//        connectionRepository.save(connection);
//        return BaseResponse.builder()
//                .statusCode(200)
//                .message("연결 생성 완료")
//                .build();
//    }
    private static MemberRole getType(Member member) {
        return member.getPhoneNumber() == null || member.getPhoneNumber().isEmpty() ? null : member.getRole();
    }

    private Member getMember(String socialId) {
        return memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(this::getNotExistMember);
    }

    private Member getMember(int memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(this::getNotExistMember);
    }

    private String createRandomNumber() {
        Random rand = new Random();
        StringBuilder randomNum = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum.append(random);
        }

        return randomNum.toString();
    }

    private CustomException getNotExistMember() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("해당하는 사용자가 존재하지 않습니다.")
                .build();
    }

    private CustomException getNotExistPhoneAuthException() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("휴대전화 인증 코드가 없습니다.")
                .build();
    }

    private CustomException getInvalidPhoneAuthCodeException(String savedCode, String requestedCode) {
        log.error("전화번호 인증코드 불일치: " + savedCode + " / " + requestedCode);
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("휴대전화 인증 코드가 일치하지 않습니다.")
                .build();
    }

    private CustomException getEmptyRoleException() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("사용자 유형이 선택되지 않았습니다.")
                .build();
    }
}
