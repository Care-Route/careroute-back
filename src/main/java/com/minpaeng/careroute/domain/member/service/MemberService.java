package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.ConnectionRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    MemberJoinResponse login(MemberJoinRequest memberJoinRequest);

    BaseResponse connectDevice(String socialId, ConnectionRequest request);
}
