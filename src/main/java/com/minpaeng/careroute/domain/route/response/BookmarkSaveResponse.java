package com.minpaeng.careroute.domain.route.response;

import com.minpaeng.careroute.domain.route.repository.entity.Bookmark;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BookmarkSaveResponse {
    private int statusCode;
    private String message;
    private int bookmarkId;
}
