package com.flab.simplesharingcar.domain;

import com.flab.simplesharingcar.constants.ReservationStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Reservation {

    private Long id;

    private Long usersId;

    private Long sharingCarsId;

    private LocalDateTime resStartTime;

    private LocalDateTime resEndTime;

    private ReservationStatus status;
}
