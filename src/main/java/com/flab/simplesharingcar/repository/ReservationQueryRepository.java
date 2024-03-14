package com.flab.simplesharingcar.repository;

import com.flab.simplesharingcar.dto.MyReservationSearchResult;
import com.flab.simplesharingcar.dto.QMyReservationSearchResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.flab.simplesharingcar.domain.QReservation.reservation;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<MyReservationSearchResult> findMyReservation(Long userId, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<MyReservationSearchResult> content = jpaQueryFactory.
                select(new QMyReservationSearchResult(
                    reservation.id,
                        reservation.sharingCar.standardCar.type,
                        reservation.sharingCar.standardCar.model,
                        reservation.status,
                        reservation.payment.price
                )).from(reservation)
                .where(reservation.user.id.eq(userId))
                .offset(offset)
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;

        if (content.size() > pageSize) {
            content.remove(pageSize);
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

}
