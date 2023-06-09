package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.domain.SharingCar;
import java.time.LocalDateTime;
import java.util.List;

public interface SharingCarRepositorySupport {

    List<SharingCar> findReserveCars(Long sharingZoneId, LocalDateTime startTime
        , LocalDateTime endTime);
}
