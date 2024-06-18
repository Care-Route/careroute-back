package com.minpaeng.careroute.domain.routine.controller;

import com.minpaeng.careroute.domain.routine.dto.request.RoutineSaveRequest;
import com.minpaeng.careroute.domain.routine.dto.response.RoutineResponse;
import com.minpaeng.careroute.domain.routine.dto.response.TargetInfoListResponse;
import com.minpaeng.careroute.domain.routine.service.RoutineService;
import com.minpaeng.careroute.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Routine", description = "일정 관리 API")
@Slf4j
@RestController
@RequestMapping("/api/routine")
@RequiredArgsConstructor
public class RoutineController {
    private final RoutineService routineService;

    @Operation(summary = "안내대상 조회", description = "안내인과 연결된 안내대상 목록 조회 API")
    @GetMapping("/targets")
    public TargetInfoListResponse getTargetInfo(Principal principal) {
        return routineService.getTargetInfo(principal.getName());
    }

    // 일정 목록 불러오기(파라미터로 날짜?)
    @GetMapping
    public List<RoutineResponse> getRoutines(Principal principal,
                                             @RequestParam int targetId,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return routineService.getRoutines(principal.getName(), targetId, date);
    }

    // 일정 상세 조회
    @GetMapping("/{routineId}")
    public RoutineResponse getRoutine(Principal principal,
                                      @PathVariable int routineId) {
        return routineService.getRoutine(principal.getName(), routineId);
    }

    @Operation(summary = "일정 등록", description = "안내인이 안내대상의 일정을 등록하는 API")
    @PostMapping
    public BaseResponse saveRoutine(Principal principal,
                                    @RequestBody RoutineSaveRequest request) {
        return routineService.saveRoutine(principal.getName(), request);
    }

    // 일정 삭제
    @Operation(summary = "일정 제거", description = "일정 아이디로 일정을 삭제하는 API")
    @DeleteMapping
    public BaseResponse deleteRoutine(Principal principal,
                                      @RequestParam int routineId) {
        return routineService.deleteRoutine(principal.getName(), routineId);
    }

}
