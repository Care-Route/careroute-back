package com.minpaeng.careroute.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BaseResponse {
    private int statusCode;
    private String message;
}
