package com.minpaeng.careroute.domain.routine.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TargetInfoListResponse {
    private int statusCode;
    private String message;
    private List<TargetInfoResponse> targetInfos;
}
