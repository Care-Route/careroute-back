package com.minpaeng.careroute.domain.routine.service;

import com.minpaeng.careroute.domain.routine.dto.request.RoutineSaveRequest;
import com.minpaeng.careroute.domain.routine.dto.response.RoutineDetailResponse;
import com.minpaeng.careroute.domain.routine.dto.response.RoutineListResponse;
import com.minpaeng.careroute.domain.routine.dto.response.TargetInfoListResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoutineService {
    TargetInfoListResponse getTargetInfo(String socialId);

    RoutineListResponse getRoutines(String name);

    RoutineDetailResponse getRoutine(String name, int routineId);

    BaseResponse saveRoutine(String socialId, RoutineSaveRequest request);

    BaseResponse deleteRoutine(String name, int routineId);
}
