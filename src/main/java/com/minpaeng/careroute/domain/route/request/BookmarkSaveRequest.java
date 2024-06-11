package com.minpaeng.careroute.domain.route.request;

import lombok.Getter;

@Getter
public class BookmarkSaveRequest {
    private String title;
    private double latitude;
    private double longitude;
}
