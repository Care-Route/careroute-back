package com.minpaeng.careroute.domain.route.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookmarkResponse {
    private int bookmarkId;
    private String title;
    private double latitude;
    private double longitude;
}
