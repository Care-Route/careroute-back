package com.minpaeng.careroute.domain.routine.dto.request;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RoutineSaveRequest {
    private int targetId;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private double startLatitude;
    private double startLogitude;
    private List<DestinationSaveRequest> destinations;
    private char isRoundTrip;
}
