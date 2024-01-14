package com.flab.simplesharingcar.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharingZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Location location;

    @Builder
    public SharingZone(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Point getPointByZoneLocation() {
        if (location == null) {
            return null;
        }
        Point point = location.toPoint();
        return point;
    }

}
