package com.minpaeng.careroute.domain.member.repository;

import com.minpaeng.careroute.domain.member.repository.entity.Connection;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
    List<Connection> findByGuide(Member member);

    Optional<Connection> findByGuideAndTarget(Member guide, Member target);

    @Query("select c from Connection c join fetch c.target where c.guide = :guide")
    List<Connection> findByGuideWithTarget(Member guide);
}
