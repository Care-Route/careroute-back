package com.minpaeng.careroute.domain.routine.repository.entity;

import com.minpaeng.careroute.domain.member.repository.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private Member guide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Member target;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    private double startLatitude;

    @Column(nullable = false)
    private double startLongitude;

    @OneToMany(mappedBy = "routine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Destination> destinations = new ArrayList<>();

    @Column(nullable = false)
    private boolean isRoundTrip;

    public void addDestinations(List<Destination> destinations) {
        this.destinations.addAll(destinations);
    }

    @Builder
    public Routine(Member guide, Member target, String title, String content,
                   LocalDate startDate, LocalDate endDate,
                   double startLatitude, double startLongitude, char isRoundTrip) {
        this.guide = guide;
        this.target = target;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.isRoundTrip = (isRoundTrip == 'Y');
    }
}
