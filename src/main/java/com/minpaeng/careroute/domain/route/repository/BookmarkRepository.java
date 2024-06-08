package com.minpaeng.careroute.domain.route.repository;

import com.minpaeng.careroute.domain.route.repository.entity.Bookmark;
import org.springframework.data.repository.CrudRepository;

public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
}
