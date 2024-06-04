package com.minpaeng.careroute.domain.mypage.service;

import com.minpaeng.careroute.global.dto.BaseResponse;

public interface MypageService {
    BaseResponse changeNickname(String socialId, String nickname);
    BaseResponse changeAddress(String socialId, String address);
    BaseResponse changeProfilePhoto(String socialId, String profilePhoto);
}
