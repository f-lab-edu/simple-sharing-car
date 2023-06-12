package com.flab.simplesharingcar.repository;

import static com.flab.simplesharingcar.domain.QReservation.reservation;
import static com.flab.simplesharingcar.domain.QSharingCar.sharingCar;
import static com.flab.simplesharingcar.domain.QStandardCar.standardCar;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.constants.CarType;
import com.flab.simplesharingcar.domain.SharingCar;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.EnumExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
        NumberExpression<Integer> orderByStatus = orderByStatus();
        NumberExpression<Integer> orderByCarType = orderByCarType();

        List<SharingCar> result = queryFactory
            .select(sharingCar)
            .from(sharingCar)
            .join(sharingCar.standardCar, standardCar).fetchJoin()
            .leftJoin(sharingCar.reservations, reservation).fetchJoin()
            .where(sharingCar.sharingZone.id.eq(sharingZoneId)
                .and(sharingCar.status.ne(CarReservationStatus.DISABLED))
                .and(searchTime.between(reservation.resStartTime, reservation.resEndTime).or(reservation.isNull())))
            .orderBy(orderByStatus.asc(), orderByCarType.asc(), standardCar.model.asc())
            .fetch();

        return result;
    }

    private NumberExpression<Integer> orderByStatus() {
        EnumExpression<CarReservationStatus> target = reservation.status.coalesce(
            sharingCar.status);
        NumberExpression<Integer> orderBy = new CaseBuilder()
            .when(target.eq(CarReservationStatus.WAITING)).then(1)
            .otherwise(2);
        return orderBy;
    }

    private NumberExpression<Integer> orderByCarType() {
        NumberExpression<Integer> orderBy = new CaseBuilder()
            .when(standardCar.type.eq(CarType.LIGHT_CAR)).then(CarType.LIGHT_CAR.getOrder())
            .when(standardCar.type.eq(CarType.SEMI_MIDSIZE_CAR)).then(CarType.SEMI_MIDSIZE_CAR.getOrder())
            .when(standardCar.type.eq(CarType.MIDSIZE_CAR)).then(CarType.MIDSIZE_CAR.getOrder())
            .when(standardCar.type.eq(CarType.LARGE_CAR)).then(CarType.LARGE_CAR.getOrder())
            .otherwise(100);
        return orderBy;
    }
}
