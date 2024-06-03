package com.minpaeng.careroute.domain.member.controller;

import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "카카오(혹은 구글)로 로그인 및 회원가입", description = "카카오(혹은 구글)로 로그인 및 회원가입 하는 API")
    @PostMapping("/login")
    public MemberJoinResponse login(@RequestBody MemberJoinRequest memberJoinRequest) {
        return memberService.login(memberJoinRequest);
    }
}
