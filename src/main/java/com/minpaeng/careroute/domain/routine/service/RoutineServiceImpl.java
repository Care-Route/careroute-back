package com.minpaeng.careroute.domain.routine.service;

import com.minpaeng.careroute.domain.member.repository.ConnectionRepository;
import com.minpaeng.careroute.domain.member.repository.MemberRepository;
import com.minpaeng.careroute.domain.member.repository.entity.Connection;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.member.repository.entity.enums.MemberRole;
import com.minpaeng.careroute.domain.routine.dto.request.RoutineSaveRequest;
import com.minpaeng.careroute.domain.routine.dto.response.RoutineDetailResponse;
import com.minpaeng.careroute.domain.routine.dto.response.RoutineListResponse;
import com.minpaeng.careroute.domain.routine.dto.response.RoutineResponse;
import com.minpaeng.careroute.domain.routine.dto.response.TargetInfoListResponse;
import com.minpaeng.careroute.domain.routine.dto.response.TargetInfoResponse;
import com.minpaeng.careroute.domain.routine.repository.RoutineRepository;
import com.minpaeng.careroute.domain.routine.repository.entity.Destination;
import com.minpaeng.careroute.domain.routine.repository.entity.Routine;
import com.minpaeng.careroute.global.dto.BaseResponse;
import com.minpaeng.careroute.global.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final MemberRepository memberRepository;
    private final ConnectionRepository connectionRepository;
    private final RoutineRepository routineRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public TargetInfoListResponse getTargetInfo(String socialId) {
        List<Object[]> results = memberRepository.findMemberBySocialIdWithConnections(socialId);
        if (results.isEmpty()) {
            throw getNotExistMemberException();
        }

        List<Member> targets = new ArrayList<>();

        for (Object[] result : results) {
            Member target = new Member((Integer) result[0], (String) result[1], (String) result[2]);
            targets.add(target);
        }

        List<TargetInfoResponse> response = targets.stream()
                .map(TargetInfoResponse::new).toList();

        return TargetInfoListResponse.builder()
                .statusCode(200)
                .message("안내 대상 조회 완료")
                .targetInfos(response)
                .build();
    }

    @Override
    public RoutineListResponse getRoutines(String socialId) {
        Member member = getMember(socialId);
        List<Routine> routines = getRoutinesOfTargets(member);
        List<RoutineResponse> responses = routines.stream().map(RoutineResponse::new).toList();
        return RoutineListResponse.builder()
                .statusCode(200)
                .message("일정 목록 조회 완료")
                .routines(responses)
                .build();
    }

    @Override
    public RoutineListResponse getTargetRoutines(String socialId, int targetId) {
        Member member = getMember(socialId);
        List<Routine> routines = getRoutinesByTargetId(member, targetId);
        List<RoutineResponse> responses = routines.stream().map(RoutineResponse::new).toList();
        return RoutineListResponse.builder()
                .statusCode(200)
                .message("일정 목록 조회 완료")
                .routines(responses)
                .build();
    }

    @Override
    public RoutineDetailResponse getRoutine(String socialId, int routineId) {
        Member member = getMember(socialId);
        Routine routine = routineRepository.findByIdWithGuideAndTargetAndDestinations(routineId)
                .orElseThrow(() -> getNotExistRoutine(routineId));

        if (member != routine.getGuide() && member != routine.getTarget())
            throw getInvalidRoutinOwnerException();

        return RoutineDetailResponse.builder()
                .statusCode(200)
                .message("일정 상세 조회 완료")
                .routine(new RoutineResponse(routine))
                .build();
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
        routineRepository.save(routine);
        List<Destination> destinations = setDestinations(request, routine);
        saveDestinationsWithNativeQuery(destinations);

        return BaseResponse.builder()
                .statusCode(200)
                .message("일정 생성 완료")
                .build();
    }

    @Override
    @Transactional
    public BaseResponse deleteRoutine(String socialId, int routineId) {
        Routine routine = routineRepository.findByIdWithGuideAndTarget(routineId)
                .orElseThrow(() -> getNotExistRoutine(routineId));

        Member member = getMember(socialId);
        if (routine.getGuide() != member && routine.getTarget() != member)
            throw getInvalidRoutinOwnerException();

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

    private Routine makeRoutineForSaveRequest(Member guide, Member target,
                                              RoutineSaveRequest request) {
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

    private List<Routine> getRoutinesOfTargets(Member member) {
        if (member.getRole() == MemberRole.GUIDE) {
            List<Member> targets = connectionRepository.findByGuideWithTarget(member)
                    .stream().map(Connection::getTarget).toList();
            return routineRepository
                    .findByMemberIdsWithGuideAndTargetAndDestinations(member, targets);
        } else if (member.getRole() == MemberRole.TARGET) {
            return routineRepository
                    .findByMemberWithGuideAndTargetAndDestinations(member);
        }
        log.info("사용자 유형 미선택 상태로 일정 조회 시도");
        return new ArrayList<>();
    }

    private List<Routine> getRoutinesByTargetId(Member member, int targetId) {
        if (member.getRole() == MemberRole.GUIDE) {
            Member target = getMember(targetId);
            if (connectionRepository.findByGuideAndTarget(member, target).isEmpty())
                throw getInvalidConnectionException();
            return routineRepository
                    .findByMemberWithGuideAndTargetAndDestinations(target);
        } else if (member.getRole() == MemberRole.TARGET) {
            return routineRepository
                    .findByMemberWithGuideAndTargetAndDestinations(member);
        }
        log.info("사용자 유형 미선택 상태로 일정 조회 시도");
        return new ArrayList<>();
    }

    private void saveDestinationsWithNativeQuery(List<Destination> destinations) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO destination (routine_id, name, destination_latitude, destination_longitude, time) VALUES ");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (Destination dest : destinations) {
            queryBuilder.append("(")
                    .append(dest.getRoutine().getId()).append(", '")
                    .append(dest.getName().replace("'", "''")).append("', ")
                    .append(dest.getDestinationLatitude()).append(", ")
                    .append(dest.getDestinationLongitude()).append(", '")
                    .append(dest.getTime().format(timeFormatter)).append("'), ");
        }
        String query = queryBuilder.substring(0, queryBuilder.length() - 2); // 마지막 콤마 제거

        em.createNativeQuery(query).executeUpdate();
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

    private CustomException getInvalidRoutinOwnerException() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("일정 소유자가 아닙니다.")
                .build();
    }

    private CustomException getInvalidConnectionException() {
        return CustomException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("안내인-안내대상 관계가 아닙니다.")
                .build();
    }
}
