package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.service.sharing.SharingCarService;
import com.flab.simplesharingcar.web.dto.CarSearchRequest;
import com.flab.simplesharingcar.web.dto.CarSearchResponse;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cars")
@RequiredArgsConstructor
public class SharingCarController {

    private final SharingCarService sharingCarService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<CarSearchResponse> list(@Valid CarSearchRequest request) {

        Long sharingZoneId = request.getSharingZoneId();
        LocalDateTime resStartTime = request.getResStartTime();
        LocalDateTime resEndTime = request.getResEndTime();

        ReservationTime reservationTime = new ReservationTime(resStartTime, resEndTime);

        List<SharingCar> sharingCarList = sharingCarService.findByZoneIdAndTime(sharingZoneId,
            reservationTime);

        CarSearchResponse response = CarSearchResponse.builder()
            .sharingCarList(sharingCarList)
            .build();
        return ResponseEntity.ok(response);
    }
}