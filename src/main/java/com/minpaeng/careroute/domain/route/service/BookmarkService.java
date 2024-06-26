package com.minpaeng.careroute.domain.route.service;

import com.minpaeng.careroute.domain.route.request.BookmarkSaveRequest;
import com.minpaeng.careroute.domain.route.response.BookmarkListResponse;
import com.minpaeng.careroute.domain.route.response.BookmarkResponse;
import com.minpaeng.careroute.domain.route.response.BookmarkSaveResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;

import java.util.List;

public interface BookmarkService {
    BookmarkSaveResponse saveSpotBookmark(String socialId, BookmarkSaveRequest request);

    BaseResponse deleteBookmark(String socialId, int bookmarkId);

    BookmarkListResponse getSpotBookmarks(String socialId);
}
