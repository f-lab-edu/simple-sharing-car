package com.flab.simplesharingcar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

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

    @JsonIgnore
    public Point getPointByZoneLocation() {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        Point point = new Point(latitude, longitude);
        return point;
    }

}
