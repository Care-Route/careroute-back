package com.minpaeng.careroute.domain.route.controller;

import com.minpaeng.careroute.domain.route.request.BookmarkSaveRequest;
import com.minpaeng.careroute.domain.route.response.BookmarkListResponse;
import com.minpaeng.careroute.domain.route.response.BookmarkSaveResponse;
import com.minpaeng.careroute.domain.route.service.BookmarkService;
import com.minpaeng.careroute.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "Route", description = "길 안내 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/route")
@RequiredArgsConstructor
public class RouteController {
    private final BookmarkService bookmarkService;

    @Operation(summary = "즐겨찾기 등록", description = "장소 즐겨찾기 등록 API")
    @PostMapping("/bookmark")
    public BookmarkSaveResponse saveSpotBookmark(Principal principal,
                                                 @RequestBody BookmarkSaveRequest request) {
        return bookmarkService.saveSpotBookmark(principal.getName(), request);
    }

    @Operation(summary = "즐겨찾기 해제", description = "장소 즐겨찾기 해제 API")
    @DeleteMapping("/bookmark/{bookmarkId}")
    public BaseResponse deleteSpotBookmark(Principal principal,
                                           @PathVariable int bookmarkId) {
        return bookmarkService.deleteBookmark(principal.getName(), bookmarkId);
    }

    @Operation(summary = "즐겨찾기 목록 조회", description = "즐겨찾기 목록 조회 API")
    @GetMapping("/bookmark")
    public BookmarkListResponse getSpotBookmarks(Principal principal) {
        return bookmarkService.getSpotBookmarks(principal.getName());
    }
}
