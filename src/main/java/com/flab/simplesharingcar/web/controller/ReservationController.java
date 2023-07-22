package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.web.dto.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve() {
        return ResponseEntity.ok(new ReservationResponse());
    }

}
