package com.flab.simplesharingcar.service.sharing;

import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.dto.CarSearchResult;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharingCarService {

    private final SharingCarRepository sharingCarRepository;

    public List<CarSearchResult> findByZoneIdAndTime(Long sharingZoneId, ReservationTime time) {

        time.validateReservationTime();

        LocalDateTime startTime = time.getStartTime();
        LocalDateTime endTime = time.getEndTime();

        List<CarSearchResult> sharingCarList = sharingCarRepository.findReserveCars(
            sharingZoneId, startTime, endTime);

        return sharingCarList;
    }
}
