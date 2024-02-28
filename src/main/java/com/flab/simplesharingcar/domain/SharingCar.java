package com.flab.simplesharingcar.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.flab.simplesharingcar.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SharingCar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_zone_id")
    private SharingZone sharingZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "standard_car_id")
    private StandardCar standardCar;

    @OneToMany(mappedBy = "sharingCar")
    private List<Reservation> reservations = new ArrayList<>();

    @Builder
    public SharingCar(SharingZone sharingZone, StandardCar standardCar,
        List<Reservation> reservations) {
        this.sharingZone = sharingZone;
        this.standardCar = standardCar;
        this.reservations = reservations;
    }

    public Integer calculatePriceByTime(ReservationTime time) {
        LocalDateTime startTime = time.getStartTime();
        LocalDateTime endTime = time.getEndTime();

        Duration between = Duration.between(startTime, endTime);
        long diffMinutes = between.toMinutes();
        Integer pricePerMinute = standardCar.getPricePerMinute();
        Integer result = Long.valueOf(diffMinutes * pricePerMinute).intValue();

        return result;
    }

}

