package com.minpaeng.careroute.domain.member.repository.redis;

import com.minpaeng.careroute.domain.member.dto.PhoneAuthDto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PhoneAuthRepository extends CrudRepository<PhoneAuthDto, String> {
    boolean existsByMemberId(int memberId);

    void deleteByMemberId(int memberId);

    Optional<PhoneAuthDto> findByMemberId(int memberId);
}
