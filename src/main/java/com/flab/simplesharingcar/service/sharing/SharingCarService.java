package com.flab.simplesharingcar.service.sharing;

import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SharingCarService {

    private final SharingCarRepository sharingCarRepository;

    public List<SharingCar> findByZoneIdAndTime(Long sharingZoneId, LocalDateTime startTime,
        LocalDateTime endTime) {

        List<SharingCar> sharingCarList = sharingCarRepository.findReserveCars(
            sharingZoneId, startTime, endTime);

        return sharingCarList;
    }
}
