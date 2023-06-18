package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.constants.CarStatus;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharingCar {

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

    @Enumerated(EnumType.STRING)
    private CarStatus status;

}
