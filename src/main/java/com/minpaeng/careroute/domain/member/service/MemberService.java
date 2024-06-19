package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.ConnectionCodeRequest;
import com.minpaeng.careroute.domain.member.dto.request.ConnectionRequest;
import com.minpaeng.careroute.domain.member.dto.request.InitialMemberInfoRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface MemberService {
    MemberJoinResponse login(MemberJoinRequest memberJoinRequest);

    BaseResponse sendAuthCode(String socialId, String phoneNumber);

    BaseResponse makeInitialInfo(String socialId, InitialMemberInfoRequest request);

    BaseResponse connectCode(String socialId, ConnectionCodeRequest request);

    BaseResponse connectDevice(String socialId, ConnectionRequest request);

    BaseResponse selectType(String idToken, String socialId, String type);
}
