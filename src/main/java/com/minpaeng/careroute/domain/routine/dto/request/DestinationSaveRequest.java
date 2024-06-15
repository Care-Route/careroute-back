package com.minpaeng.careroute.domain.routine.dto.request;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class DestinationSaveRequest {
    private double destinationLatitude;
    private double destinationLongitude;
    private LocalTime time;
}
