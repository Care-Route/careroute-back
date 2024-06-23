package com.minpaeng.careroute.domain.routine.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "destination")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Routine routine;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double destinationLatitude;

    @Column(nullable = false)
    private double destinationLongitude;

    @Column
    private LocalTime time;

    @Builder
    public Destination(Routine routine, String name,
                       double destinationLatitude, double destinationLongitude, LocalTime time) {
        this.routine = routine;
        this.name = name;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.time = time;
    }
}
