package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.ConnectionProposalRequest;
import com.minpaeng.careroute.domain.member.dto.request.InitialMemberInfoRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.dto.response.MemberRoleResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    MemberJoinResponse login(MemberJoinRequest memberJoinRequest);

    BaseResponse sendAuthCode(String socialId, String phoneNumber);

    BaseResponse makeInitialInfo(String socialId, InitialMemberInfoRequest request);

    BaseResponse selectType(String idToken, String socialId, String type);

    BaseResponse makeConnectionProposal(String socialId, ConnectionProposalRequest request);

    BaseResponse makeConnection(String idToken, int memberId);

    MemberRoleResponse getMemberRoleByPhoneNumber(String phoneNumber);
}
