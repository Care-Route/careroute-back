package com.minpaeng.careroute.domain.routine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoutineDetailResponse {
    private int statusCode;
    private String message;
    private RoutineResponse routine;
}
