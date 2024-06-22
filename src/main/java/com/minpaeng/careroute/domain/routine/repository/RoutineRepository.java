package com.minpaeng.careroute.domain.routine.repository;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.routine.repository.entity.Routine;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends CrudRepository<Routine, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Routine r WHERE r.id = :routineId")
    void deleteRoutineById(@Param("routineId") int routineId);

    @Query("select r from Routine r join fetch r.guide join fetch r.target where r.id = :routineId")
    Optional<Routine> findByIdWithGuideAndTarget(@Param("routineId") int routineId);

    @Query("select r from Routine  r " +
            "join fetch r.guide join fetch r.target left join fetch r.destinations " +
            "where r.target = :member and :date between r.startDate and r.endDate")
    List<Routine> findByMemberAndDateWithGuideAndTargetAndDestinations(@Param("member") Member member,
                                                                       @Param("date") LocalDate date);

    @Query("select r from Routine  r " +
            "join fetch r.guide join fetch r.target left join fetch r.destinations " +
            "where r.target = :member")
    List<Routine> findByMemberWithGuideAndTargetAndDestinations(@Param("member") Member member);

    @Query("select r from Routine r " +
            "join fetch r.guide join fetch r.target join fetch r.destinations " +
            "where r.id = :routineId")
    Optional<Routine> findByIdWithGuideAndTargetAndDestinations(@Param("routineId") int routineId);

    @Query("select r from Routine r " +
            "join fetch r.guide join fetch r.target join fetch r.destinations " +
            "where r.guide = :guide and r.target in :targets")
    List<Routine> findByMemberIdsWithGuideAndTargetAndDestinations(@Param("guide") Member guide,
                                                     @Param("targetIds") List<Member> targets);
}
