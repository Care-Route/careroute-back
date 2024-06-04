package com.minpaeng.careroute.domain.member.repository;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findMemberBySocialId(String socialId);

    Optional<Member> findMemberByPhoneNumber(String phoneNumber);

}
