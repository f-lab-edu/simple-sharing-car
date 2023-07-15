package com.flab.simplesharingcar.repository;

import static com.flab.simplesharingcar.domain.QReservation.reservation;
import static com.flab.simplesharingcar.domain.QSharingCar.sharingCar;
import static com.flab.simplesharingcar.domain.QStandardCar.standardCar;

import com.flab.simplesharingcar.constants.CarType;
import com.flab.simplesharingcar.constants.ReservationStatus;
import com.flab.simplesharingcar.dto.CarSearchResult;
import com.flab.simplesharingcar.dto.QCarSearchResult;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.EnumExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.QueryHints;

@RequiredArgsConstructor
public class SharingCarRepositorySupportImpl implements SharingCarRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CarSearchResult> findReserveCars(Long sharingZoneId, LocalDateTime startTime,
        LocalDateTime endTime) {

        DateTimeExpression<LocalDateTime> searchStartTime = Expressions.asDateTime(
            startTime);
        DateTimeExpression<LocalDateTime> searchEndTime = Expressions.asDateTime(
            endTime);

        NumberExpression<Integer> orderByStatus = orderByStatus();
        NumberExpression<Integer> orderByCarType = orderByCarType();

        List<CarSearchResult> result = queryFactory
            .select(new QCarSearchResult(
                sharingCar,
                searchStartTime,
                searchEndTime,
                reservation.status
            ))
            .from(sharingCar)
            .join(sharingCar.standardCar, standardCar).fetchJoin()
            .leftJoin(sharingCar.reservations, reservation)
            .on(searchStartTime.goe(reservation.resStartTime).and(searchStartTime.before(reservation.resEndTime))
                .or((searchEndTime.goe(reservation.resStartTime).and(searchEndTime.before(reservation.resEndTime))))
            )
            .where(sharingCar.sharingZone.id.eq(sharingZoneId))
            .orderBy(orderByStatus.asc(), orderByCarType.asc(), standardCar.model.asc())
            .fetch()
            .stream()
            .distinct()
            .collect(Collectors.toList());

        return result;
    }

    private NumberExpression<Integer> orderByStatus() {
        EnumExpression<ReservationStatus> target = ifNullStatus();
        NumberExpression<Integer> orderBy = new CaseBuilder()
            .when(target.eq(ReservationStatus.WAITING)).then(1)
            .otherwise(2);
        return orderBy;
    }

    private EnumExpression<ReservationStatus> ifNullStatus() {
        EnumExpression<ReservationStatus> status = new CaseBuilder()
            .when(reservation.id.isNull()).then(ReservationStatus.WAITING)
            .otherwise(reservation.status);
        return status;
    }

    private NumberExpression<Integer> orderByCarType() {
        NumberExpression<Integer> orderBy = new CaseBuilder()
            .when(standardCar.type.eq(CarType.LIGHT_CAR)).then(CarType.LIGHT_CAR.getOrder())
            .when(standardCar.type.eq(CarType.SEMI_MIDSIZE_CAR))
            .then(CarType.SEMI_MIDSIZE_CAR.getOrder())
            .when(standardCar.type.eq(CarType.MIDSIZE_CAR)).then(CarType.MIDSIZE_CAR.getOrder())
            .when(standardCar.type.eq(CarType.LARGE_CAR)).then(CarType.LARGE_CAR.getOrder())
            .otherwise(100);
        return orderBy;
    }

}
