package com.minpaeng.careroute.domain.mypage.controller;

import com.minpaeng.careroute.domain.mypage.dto.request.AddressUpdateRequest;
import com.minpaeng.careroute.domain.mypage.dto.request.NicknameUpdateRequest;
import com.minpaeng.careroute.domain.mypage.dto.response.ProfileImageUpdateResponse;
import com.minpaeng.careroute.domain.mypage.dto.response.ProfileInfoResponse;
import com.minpaeng.careroute.domain.mypage.service.MypageService;
import com.minpaeng.careroute.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Tag(name = "Mypage", description = "마이페이지 API")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    @GetMapping
    public ProfileInfoResponse getProfileInfo(Principal principal) {
        return mypageService.getProfileInfo(principal.getName());
    }

    @PutMapping("/nickname")
    public BaseResponse changeNickname(Principal principal,
                                       @RequestBody NicknameUpdateRequest request) {
        return mypageService.changeNickname(principal.getName(), request.getNickname());
    }

    @PutMapping("/address")
    public BaseResponse changeAddress(Principal principal,
                                      @RequestBody AddressUpdateRequest request) {
        return mypageService.changeAddress(principal.getName(), request.getAddress());
    }

    @PutMapping("/profile")
    public ProfileImageUpdateResponse changeProfileImage(Principal principal,
                                                         @RequestPart(name = "image") MultipartFile image) {
        return mypageService.changeProfilePhoto(principal.getName(), image);
    }
}
