package com.minpaeng.careroute.domain.mypage.service;

import com.minpaeng.careroute.domain.mypage.dto.response.ProfileImageUpdateResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MypageService {
    BaseResponse changeNickname(String socialId, String nickname);
    BaseResponse changeAddress(String socialId, String address);
    ProfileImageUpdateResponse changeProfilePhoto(String socialId, MultipartFile image);
}
