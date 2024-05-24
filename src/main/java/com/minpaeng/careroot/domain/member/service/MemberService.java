package com.minpaeng.careroot.domain.member.service;

import com.minpaeng.careroot.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroot.domain.member.dto.response.MemberJoinResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    ResponseEntity<MemberJoinResponse> login(String type, MemberJoinRequest memberJoinRequest);
}
