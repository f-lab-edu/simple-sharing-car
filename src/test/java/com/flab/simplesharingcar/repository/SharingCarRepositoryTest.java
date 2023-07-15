package com.flab.simplesharingcar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.constants.CarType;
import com.flab.simplesharingcar.constants.ReservationStatus;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.StandardCar;
import com.flab.simplesharingcar.dto.CarSearchResult;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Import({QuerydslConfig.class})
class SharingCarRepositoryTest {

    @Autowired
    private SharingCarRepository sharingCarRepository;

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    public void initClass() {
    }

    @Test
    public void 차량_검색_테스트() {
        // given
        Long sharingZoneId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime to = now.plus(1, ChronoUnit.HOURS);
        // when
        List<CarSearchResult> reservatableCar = sharingCarRepository.findReserveCars(sharingZoneId, now, to);
        // then
        assertThat(reservatableCar).hasSize(2);
    }

    @Test
    public void 예약_상태_정렬_확인() {
        // given
        Long sharingZoneId = 2L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime to = now.plus(1, ChronoUnit.HOURS);
        // when
        List<CarSearchResult> reservatableCar = sharingCarRepository.findReserveCars(sharingZoneId, now, to);
        // then
        // 2L -> NOT_RESERVATION, 4L -> ENABLED
        CarSearchResult sharingCar = reservatableCar.get(1);
        Long id = sharingCar.getId();
        assertThat(id).isEqualTo(2L);
    }

    @Test
    public void 차량_타입_정렬() {
        // given
        Long sharingZoneId = 3L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime to = now.plus(1, ChronoUnit.HOURS);
        // when
        List<CarSearchResult> reservatableCar = sharingCarRepository.findReserveCars(sharingZoneId, now, to);
        // then
        // 0 LIGHT_CAR, 1 SEMI_MIDSIZE_CAR, 2 MIDSIZE_CAR, 3 LARGE_CAR,
        CarSearchResult sharingCar = reservatableCar.get(1);
        String type = sharingCar.getType();
        assertThat(type).isEqualTo(CarType.SEMI_MIDSIZE_CAR.getName());
    }

}