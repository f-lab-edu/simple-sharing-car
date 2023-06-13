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

    public List<SharingCar> findByZoneIdAndTime(Long sharingZoneId, LocalDateTime searchTime) {
        List<SharingCar> sharingCarList = sharingCarRepository.findReservatableCar(
            sharingZoneId, searchTime);

        List<SharingCar> resultList = sharingCarList.stream()
            .map(sharingCar -> SharingCar.newInstanceByTime(sharingCar, searchTime))
            .collect(Collectors.toList());
        return resultList;
    }
}
