package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.security.KakaoOauthHelper;
import com.minpaeng.careroute.domain.member.security.OIDCDecodePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final KakaoOauthHelper kakaoOauthHelper;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<MemberJoinResponse> login(String type,
                                                    MemberJoinRequest memberJoinRequest) {
        if (type.equals("kakao")) loginByKakao(memberJoinRequest);
        return null;
    }

    @Override
    public void test(String idToken) {
        System.out.println("서비스 진입: " + idToken);
        OIDCDecodePayload oidcDecodePayload = kakaoOauthHelper.getOIDCDecodePayload(idToken);
        log.info(oidcDecodePayload.toString());
    }

    private void loginByKakao(MemberJoinRequest memberJoinRequest) {

    }
}
