package com.minpaeng.careroute.domain.routine.dto.response;

import com.minpaeng.careroute.domain.routine.repository.entity.Destination;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class DestinationResponse {
    private final int destinationId;
    private final String name;
    private final double destinationLatitude;
    private final double destinationLongitude;
    private final LocalTime time;

    public DestinationResponse(Destination destination) {
        this.destinationId = destination.getId();
        this.name = destination.getName();
        this.destinationLatitude = destination.getDestinationLatitude();
        this.destinationLongitude = destination.getDestinationLongitude();
        this.time = destination.getTime();
    }
}
