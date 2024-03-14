package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.dto.MyReservationSearchResult;
import com.flab.simplesharingcar.service.reservation.MyReservationService;
import com.flab.simplesharingcar.service.reservation.ReservationService;
import com.flab.simplesharingcar.web.dto.MyReservationResponse;
import com.flab.simplesharingcar.web.dto.ReservationRequest;
import com.flab.simplesharingcar.web.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final MyReservationService myReservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationRequest request,
            HttpServletRequest servletRequest) {
        Long paymentId = request.getPaymentId();

        Long sharingCarId = request.getSharingCarId();

        LocalDateTime resStartTime = request.getResStartTime();
        LocalDateTime resEndTime = request.getResEndTime();
        ReservationTime reservationTime = new ReservationTime(resStartTime, resEndTime);

        Long userId = getUserIdByServletRequest(servletRequest);

        Reservation reserve = reservationService.reserve(sharingCarId, userId, paymentId, reservationTime);
        ReservationResponse response = ReservationResponse.from(reserve);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<MyReservationResponse> myReservations(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                HttpServletRequest servletRequest) {

        Long userId = getUserIdByServletRequest(servletRequest);
        Slice<MyReservationSearchResult> slice = myReservationService.findSliced(userId, pageable);

        MyReservationResponse response = new MyReservationResponse(slice);

        return ResponseEntity.ok(response);
    }

    private Long getUserIdByServletRequest(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        User loginUser = (User) session.getAttribute(SessionKey.LOGIN_USER);
        return loginUser.getId();
    }

}
