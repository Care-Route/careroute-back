package com.minpaeng.careroute.domain.member.controller;

import com.minpaeng.careroute.domain.member.dto.request.ConnectDeviceRequest;
import com.minpaeng.careroute.domain.member.dto.request.ConnectionProposalRequest;
import com.minpaeng.careroute.domain.member.dto.request.InitialMemberInfoRequest;
import com.minpaeng.careroute.domain.member.dto.request.MemberJoinRequest;
import com.minpaeng.careroute.domain.member.dto.request.SearchUserRequest;
import com.minpaeng.careroute.domain.member.dto.request.SendAuthRequest;
import com.minpaeng.careroute.domain.member.dto.request.TypeSaveRequest;
import com.minpaeng.careroute.domain.member.dto.response.MemberJoinResponse;
import com.minpaeng.careroute.domain.member.dto.response.MemberRoleResponse;
import com.minpaeng.careroute.domain.member.service.MemberService;
import com.minpaeng.careroute.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate template;

    @Operation(summary = "카카오(혹은 구글)로 로그인 및 회원가입", description = "카카오(혹은 구글)로 로그인 및 회원가입 하는 API")
    @PostMapping("/login")
    public MemberJoinResponse login(@RequestBody MemberJoinRequest request) {
        return memberService.login(request);
    }

    @Operation(summary = "전화번호 인증 번호 발송", description = "인증 번호 발송 API")
    @PostMapping("/auth")
    public BaseResponse sendAuthCode(Principal principal, @RequestBody SendAuthRequest request) {
        return memberService.sendAuthCode(principal.getName(), request.getPhoneNumber());
    }

    @Operation(summary = "전화번호 및 닉네임 설정", description = "인증 번호 검증 후 전화번호와 닉네임을 설정하는 API")
    @PostMapping("/account")
    public BaseResponse makeInitialInfo(Principal principal,
                                        @RequestBody InitialMemberInfoRequest request) {
        return memberService.makeInitialInfo(principal.getName(), request);
    }

    @Operation(summary = "사용자 타입 지정", description = "GUIDE(안내인), TARGET(안내 대상)으로 사용자 유형을 지정하는 API")
    @PostMapping("/type")
    public BaseResponse selectType(HttpServletRequest servletRequest,
                                   Principal principal,
                                   @RequestBody TypeSaveRequest request) {
        String idToken = servletRequest.getHeader("Authorization");
        return memberService.selectType(idToken, principal.getName(), request.getType());
    }

//    @Operation(summary = "기기연결 요청", description = "기기 연결 요청 전송 API")
//    @PostMapping("/connection/code")
//    public BaseResponse connectCode(Principal principal,
//                                           @RequestBody ConnectionCodeRequest request) {
//        return memberService.connectCode(principal.getName(), request);
//    }
//
//    @Operation(summary = "기기 연결", description = "인증코드 확인 및 연결 수립 API")
//    @PostMapping("/connection")
//    public BaseResponse connectDevice(Principal principal,
//                                      @RequestBody ConnectionRequest request) {
//        return memberService.connectDevice(principal.getName(), request);
//    }

    @Operation(summary = "전화번호로 사용자 타입 검색", description = "전화번호로 사용자 타입을 조회하는 API")
    @PostMapping("/search/type")
    public MemberRoleResponse getMemberRoleByPhoneNumber(@RequestBody SearchUserRequest request) {
        return memberService.getMemberRoleByPhoneNumber(request.getPhoneNumber());
    }


    @Operation(summary = "기기 연결 요청", description = "기기 연결 요청 API")
    @MessageMapping(value = "/connection/proposal")
    public BaseResponse connectionProposal(Principal principal,
                                           @RequestBody ConnectionProposalRequest request) {
        return memberService.makeConnectionProposal(principal.getName(), request);
    }

    @Operation(summary = "기기 연결 요청 수락", description = "기기 연결 요청 수락 API")
    @PostMapping("/connection")
    public BaseResponse connectDevice(Principal principal,
                                       @RequestBody ConnectDeviceRequest request) {
        return memberService.makeConnection(principal.getName(), request.getMemberId());
    }
}
