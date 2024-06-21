package com.minpaeng.careroute.domain.member.repository;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findMemberBySocialId(String socialId);

    Optional<Member> findMemberByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT t.member_id AS t_id, " +
            "t.nickname AS t_nickname,  " +
            "t.profile_image_path AS t_profile_image_path " +
            "FROM member m " +
            "LEFT JOIN connection c ON m.member_id = c.guide_id " +
            "LEFT JOIN member t ON t.member_id = c.target_id " +
            "WHERE m.social_id = :socialId", nativeQuery = true)
    List<Object[]> findMemberBySocialIdWithConnections(String socialId);

}
