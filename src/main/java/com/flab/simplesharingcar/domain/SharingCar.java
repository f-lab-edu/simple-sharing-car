package com.flab.simplesharingcar.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SharingCar {

    private Long id;

    private Long sharingZoneId;

    private Long standardCarId;

}
