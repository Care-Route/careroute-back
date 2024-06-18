package com.minpaeng.careroute.domain.member.repository;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findMemberBySocialId(String socialId);

    Optional<Member> findMemberByPhoneNumber(String phoneNumber);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.connections c LEFT JOIN FETCH c.target WHERE m.socialId = :socialId")
    Optional<Member> findMemberBySocialIdWithConnections(String socialId);

}
