package com.minpaeng.careroot.domain.member.repository;

import com.minpaeng.careroot.domain.member.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
