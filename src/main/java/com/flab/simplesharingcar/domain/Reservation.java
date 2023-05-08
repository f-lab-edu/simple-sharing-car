package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.constants.ReservationStatus;
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

    private ReservationStatus status;
}
