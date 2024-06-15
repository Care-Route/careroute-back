package com.minpaeng.careroute.domain.routine.service;

import com.minpaeng.careroute.domain.member.repository.ConnectionRepository;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.routine.dto.request.RoutineSaveRequest;
import com.minpaeng.careroute.domain.routine.dto.response.RoutineResponse;
import com.minpaeng.careroute.domain.routine.dto.response.TargetInfoListResponse;
import com.minpaeng.careroute.domain.routine.dto.response.TargetInfoResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final MemberRepository memberRepository;
    private final ConnectionRepository connectionRepository;

    @Override
    public TargetInfoListResponse getTargetInfo(String socialId) {
        Member member = memberRepository.findMemberBySocialIdWithConnections(socialId)
                .orElseThrow(() -> CustomException.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("사용자가 존재하지 않습니다.")
                        .build());

        List<TargetInfoResponse> response = member.getConnections().stream().map(c -> new TargetInfoResponse(c.getTarget())).toList();
        return TargetInfoListResponse.builder()
                .statusCode(200)
                .message("안내 대상 조회 완료")
                .targetInfos(response)
                .build();
    }

    @Override
    public List<RoutineResponse> getRoutines(String name, int targetId, LocalDate date) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponse saveRoutine(String socialId, RoutineSaveRequest request) {
        return null;
    }

    @Override
    public RoutineResponse getRoutine(String name, int routineId) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponse deleteRoutine(String name, int routineId) {
        return null;
    }

    private Member getMemger(String socialId) {
        return memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(() -> new IllegalStateException("해당하는 사용자가 존재하지 않습니다."));
    }
}
