package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.dto.CarSearchResult;
import java.time.LocalDateTime;
import java.util.List;

public interface SharingCarRepositorySupport {

    List<CarSearchResult> findReserveCars(Long sharingZoneId, LocalDateTime startTime
        , LocalDateTime endTime);
}
