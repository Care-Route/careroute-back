package com.minpaeng.careroute.domain.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProfileImageUpdateResponse {
    private int statusCode;
    private String message;
    private String imageUrl;
}
