package com.minpaeng.careroute.domain.routine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RoutineListResponse {
    private int statusCode;
    private String message;
    private List<RoutineResponse> routines;
}
