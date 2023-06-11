package com.flab.simplesharingcar.repository;

import static com.flab.simplesharingcar.domain.QReservation.reservation;
import static com.flab.simplesharingcar.domain.QSharingCar.sharingCar;
import static com.flab.simplesharingcar.domain.QStandardCar.standardCar;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.SharingCar;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SharingCarRepositorySupportImpl implements SharingCarRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SharingCar> findReservatableCar(Long sharingZoneId, LocalDateTime resStartTime) {
        DateTimeExpression<LocalDateTime> searchTime = Expressions.asDateTime(
            resStartTime);
        List<SharingCar> result = queryFactory
            .select(sharingCar)
            .from(sharingCar)
            .join(sharingCar.standardCar, standardCar)
            .leftJoin(sharingCar.reservations, reservation)
            .on(searchTime.between(reservation.resStartTime, reservation.resEndTime))
            .where(sharingCar.sharingZone.id.eq(sharingZoneId)
                .and(sharingCar.status.ne(CarReservationStatus.DISABLED)))
            .fetch();
        return result;
    }
}
