package com.minpaeng.careroute.domain.route.service;

import com.minpaeng.careroute.domain.route.request.BookmarkSaveRequest;
import com.minpaeng.careroute.domain.route.response.BookmarkResponse;
import com.minpaeng.careroute.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    @Transactional
    @Override
    public BaseResponse saveSpotBookmark(String socialId, BookmarkSaveRequest request) {
        return null;
    }

    @Override
    public List<BookmarkResponse> getSpotBookmarks(String socialId) {
        return null;
    }
}
