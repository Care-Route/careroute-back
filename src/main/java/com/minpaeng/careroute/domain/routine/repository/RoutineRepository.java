package com.minpaeng.careroute.domain.routine.repository;

import com.minpaeng.careroute.domain.routine.repository.entity.Routine;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RoutineRepository extends CrudRepository<Routine, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Routine r WHERE r.id = :routineId")
    void deleteRoutineById(@Param("routineId") int routineId);

    @Query("select r from Routine r join fetch r.guide join fetch r.target where r.id = :routineId")
    Optional<Routine> findByIdWithGuideAndTarget(@Param("routineId") int routineId);
}
