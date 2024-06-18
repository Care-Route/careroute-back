package com.minpaeng.careroute.domain.routine.dto.response;

import com.minpaeng.careroute.domain.routine.repository.entity.Destination;
import com.minpaeng.careroute.domain.routine.repository.entity.Routine;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RoutineResponse {
    private final int routineId;
    private final String title;
    private final String content;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double startLatitude;
    private final double startLongitude;
    private final List<DestinationResponse> destinations;
    private final boolean isRoundTrip;

    public RoutineResponse(Routine routine) {
        this.routineId = routine.getId();
        this.title = routine.getTitle();
        this.content = routine.getContent();
        this.startDate = routine.getStartDate();
        this.endDate = routine.getEndDate();
        this.startLatitude = routine.getStartLatitude();
        this.startLongitude = routine.getStartLongitude();
        this.destinations = makeDestinationResponses(routine.getDestinations());
        this.isRoundTrip = routine.isRoundTrip();
    }

    private List<DestinationResponse> makeDestinationResponses(List<Destination> destinations) {
        return destinations.stream().map(DestinationResponse::new).toList();
    }
}
