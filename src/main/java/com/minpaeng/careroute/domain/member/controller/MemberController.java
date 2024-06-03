package com.minpaeng.careroute.domain.member.controller;

import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/test")
    public String test(@RequestBody MemberJoinRequest memberJoinRequest) {
        System.out.println("컨트롤러 진입: " + memberJoinRequest.getAuthorizationToken());
        memberService.test(memberJoinRequest.getAuthorizationToken());
        return "캐싱 완료";
    }

    @GetMapping("/test2")
    public String test2() {
        return "테스트 문제업슴..";
    }
}
