package com.minpaeng.careroute.domain.routine.service;

import com.minpaeng.careroute.domain.member.repository.ConnectionRepository;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.routine.dto.request.RoutineSaveRequest;
import com.minpaeng.careroute.domain.routine.dto.response.RoutineResponse;
import com.minpaeng.careroute.domain.routine.dto.response.TargetInfoListResponse;
import com.minpaeng.careroute.domain.routine.dto.response.TargetInfoResponse;
import com.minpaeng.careroute.domain.routine.repository.RoutineRepository;
import com.minpaeng.careroute.domain.routine.repository.entity.Destination;
import com.minpaeng.careroute.domain.routine.repository.entity.Routine;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final MemberRepository memberRepository;
    private final ConnectionRepository connectionRepository;
    private final RoutineRepository routineRepository;

    @Override
    public TargetInfoListResponse getTargetInfo(String socialId) {
        Member member = memberRepository.findMemberBySocialIdWithConnections(socialId)
                .orElseThrow(this::getNotExistMemberException);

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
        Member guide = getMember(socialId);
        Member target = getMember(request.getTargetId());
        if (connectionRepository.findByGuideAndTarget(guide, target).isEmpty())
            throw getNotTargetException();

        if (request.getDestinations() != null && request.getDestinations().size() > 3)
            throw getDestinationLimitException();

        Routine routine = makeRoutineForSaveRequest(guide, target, request);
        routine.addDestinations(setDestinations(request, routine));
        routineRepository.save(routine);

        return BaseResponse.builder()
                .statusCode(200)
                .message("일정 생성 완료")
                .build();
    }

    @Override
    public RoutineResponse getRoutine(String name, int routineId) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponse deleteRoutine(String name, int routineId) {
        // 올바른 사용자의 요청인지 확인하는 로직 추가 필요

        routineRepository.findById(routineId)
                .orElseThrow(() -> getNotExistRoutine(routineId));
        routineRepository.deleteRoutineById(routineId);
        return BaseResponse.builder()
                .statusCode(200)
                .message("일정 삭제 완료: " + routineId)
                .build();
    }

    private Member getMember(String socialId) {
        return memberRepository.findMemberBySocialId(socialId)
                .orElseThrow(this::getNotExistMemberException);
    }

    private Member getMember(int memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(this::getNotExistMemberException);
    }

    private Routine makeRoutineForSaveRequest(Member guide, Member target, RoutineSaveRequest request) {
        return Routine.builder()
                .guide(guide)
                .target(target)
                .title(request.getTitle())
                .content(request.getContent())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startLatitude(request.getStartLatitude())
                .startLongitude(request.getStartLogitude())
                .isRoundTrip(request.getIsRoundTrip())
                .build();
    }

    private List<Destination> setDestinations(RoutineSaveRequest request, Routine routine) {
        return request.getDestinations().stream()
                .map(d -> Destination.builder()
                        .routine(routine)
                        .name(d.getName())
                        .destinationLatitude(d.getDestinationLatitude())
                        .destinationLongitude(d.getDestinationLongitude())
                        .time(d.getTime())
                        .build())
                .toList();
    }

    private CustomException getNotExistMemberException() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.NO_CONTENT.value())
                .message("해당하는 사용자가 존재하지 않습니다.")
                .build();
    }

    private CustomException getNotTargetException() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.NO_CONTENT.value())
                .message("안내대상으로 등록된 사용자가 아닙니다.")
                .build();
    }

    private CustomException getDestinationLimitException() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.PROCESSING.value())
                .message("최대 세 개의 목적지를 설정할 수 있습니다.")
                .build();
    }

    private CustomException getNotExistRoutine(int routineId) {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("아이디 " + routineId + "에 해당하는 일정 데이터가 존재하지 않습니다.")
                .build();
    }
}
