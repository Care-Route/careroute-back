package com.minpaeng.careroute.domain.member.service;

import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    ResponseEntity<MemberJoinResponse> login(String type, MemberJoinRequest memberJoinRequest);

    void test(String idToken);
}
