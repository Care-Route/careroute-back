package com.minpaeng.careroute.domain.member.repository.redis;

import com.minpaeng.careroute.domain.member.dto.ConnectionDto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConnectionAuthRepository extends CrudRepository<ConnectionDto, String> {
    Optional<ConnectionDto> findByFromNumberAndToNumber(String fromNumber, String toNumber);
}
