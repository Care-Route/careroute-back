package com.minpaeng.careroute.domain.route.repository;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import com.minpaeng.careroute.domain.route.repository.entity.Bookmark;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
    List<Bookmark> findByMember(Member member);

    @Query("select b from Bookmark b join fetch b.member where b.id = :bookmarkId")
    Optional<Bookmark> findByByIdWithMember(@Param("bookmarkId") int bookmarkId);
}
