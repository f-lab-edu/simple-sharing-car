package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SharingZone extends BaseEntity {

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
