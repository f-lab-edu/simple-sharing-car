package com.flab.simplesharingcar.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Column(columnDefinition="decimal")
    private Double latitude;

    @Column(columnDefinition="decimal")
    private Double longitude;

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point toPoint() {
        Point point = new Point(longitude, latitude);
        return point;
    }
}
