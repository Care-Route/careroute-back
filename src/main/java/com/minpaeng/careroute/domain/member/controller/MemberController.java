package com.minpaeng.careroute.domain.member.controller;

import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public MemberJoinResponse login(@RequestBody MemberJoinRequest memberJoinRequest) {
        return memberService.login(memberJoinRequest);
    }

    @GetMapping("/test2")
    public String test2() {
        return "테스트 문제업슴..";
    }
}
