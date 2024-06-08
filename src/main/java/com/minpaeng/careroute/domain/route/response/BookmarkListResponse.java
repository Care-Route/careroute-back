package com.minpaeng.careroute.domain.route.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BookmarkListResponse {
    private int statusCode;
    private String message;
    private List<BookmarkResponse> bookmarks;
}
