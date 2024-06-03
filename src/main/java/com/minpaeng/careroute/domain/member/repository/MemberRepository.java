package com.minpaeng.careroute.domain.member.repository;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
