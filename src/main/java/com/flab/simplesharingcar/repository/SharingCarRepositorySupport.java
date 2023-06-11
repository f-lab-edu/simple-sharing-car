package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.domain.SharingCar;
import java.time.LocalDateTime;
import java.util.List;

public interface SharingCarRepositorySupport {

    List<SharingCar> findReservatableCar(Long sharingZoneId, LocalDateTime resStartTime);
}
