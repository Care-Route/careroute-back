package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    MemberJoinResponse login(MemberJoinRequest memberJoinRequest);
}
