package com.minpaeng.careroute.domain.route.service;

import com.minpaeng.careroute.domain.route.request.BookmarkSaveRequest;
import com.minpaeng.careroute.domain.route.response.BookmarkResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;

import java.util.List;

public interface BookmarkService {
    BaseResponse saveSpotBookmark(String socialId, BookmarkSaveRequest request);

    List<BookmarkResponse> getSpotBookmarks(String socialId);
}
