package com.minpaeng.careroute.domain.member.repository;

import com.minpaeng.careroute.domain.member.repository.entity.Connection;
import com.minpaeng.careroute.domain.member.repository.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
    List<Connection> findByGuide(Member member);
}
