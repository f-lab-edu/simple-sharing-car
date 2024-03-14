package com.flab.simplesharingcar.service.reservation;

import com.flab.simplesharingcar.dto.MyReservationSearchResult;
import com.flab.simplesharingcar.repository.ReservationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyReservationService {

    private final ReservationQueryRepository reservationQueryRepository;

    public Slice<MyReservationSearchResult> findSliced(Long userId, Pageable pageable) {
        return reservationQueryRepository.findMyReservation(userId, pageable);
    }

}
