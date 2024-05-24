package com.minpaeng.careroot.domain.member.service;

import com.minpaeng.careroot.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroot.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroot.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<MemberJoinResponse> login(String type,
                                                    MemberJoinRequest memberJoinRequest) {
        return null;
    }
}
