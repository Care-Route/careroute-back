package com.minpaeng.careroute.domain.routine.repository;

import com.minpaeng.careroute.domain.routine.repository.entity.Routine;
import org.springframework.data.repository.CrudRepository;

public interface RoutineRepository extends CrudRepository<Routine, Integer> {
}
