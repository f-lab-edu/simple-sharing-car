package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Reservation {

    private Long id;

    private Long userId;

    private Long sharingCarId;

    private LocalDateTime resStartTime;

    private LocalDateTime resEndTime;

    private CarReservationStatus status;
}
