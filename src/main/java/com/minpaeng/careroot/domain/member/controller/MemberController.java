package com.minpaeng.careroot.domain.member.controller;

import com.minpaeng.careroot.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroot.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroot.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<MemberJoinResponse> login(@RequestParam String type,
                                                         MemberJoinRequest memberJoinRequest) {
        return memberService.login(type, memberJoinRequest);
    }
}
