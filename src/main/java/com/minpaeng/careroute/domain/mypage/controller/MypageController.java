package com.minpaeng.careroute.domain.mypage.controller;

import com.minpaeng.careroute.domain.mypage.dto.request.NicknameUpdateRequest;
import com.minpaeng.careroute.domain.mypage.service.MypageService;
import com.minpaeng.careroute.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "Mypage", description = "마이페이지 API")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    @PutMapping("/nickname")
    public BaseResponse changeNickname(Principal principal, @RequestBody NicknameUpdateRequest request) {
        return mypageService.changeNickname(principal.getName(), request.getNickname());
    }
}
