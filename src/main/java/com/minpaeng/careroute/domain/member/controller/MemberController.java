package com.minpaeng.careroute.domain.member.controller;

import com.minpaeng.careroute.domain.member.dto.request.ConnectionRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.service.MemberService;
import com.minpaeng.careroute.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "Member", description = "사용자 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "카카오(혹은 구글)로 로그인 및 회원가입", description = "카카오(혹은 구글)로 로그인 및 회원가입 하는 API")
    @PostMapping("/login")
    public MemberJoinResponse login(@RequestBody MemberJoinRequest request) {
        return memberService.login(request);
    }

    @Operation(summary = "기기 연결", description = "기기 연결 API")
    @PostMapping("/connection")
    public BaseResponse connectDevice(Principal principal,
                                      @RequestBody ConnectionRequest request) {
        log.info("socialId: " + principal.getName());
        return memberService.connectDevice(principal.getName(), request);
    }
}
