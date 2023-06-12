package com.flab.simplesharingcar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.constants.CarType;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.StandardCar;
import java.time.LocalDateTime;
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
        // when
        List<SharingCar> reservatableCar = sharingCarRepository.findReservatableCar(sharingZoneId,
            now);
        // then
        assertThat(reservatableCar).hasSize(1);
    }

    @Test
    public void 차량_예약_상태_확인() {
        // given
        Long sharingZoneId = 1L;
        LocalDateTime now = LocalDateTime.now();
        // when
        List<SharingCar> reservatableCar = sharingCarRepository.findReservatableCar(sharingZoneId,
            now);
        // then
        SharingCar sharingCar = reservatableCar.get(0);
        Reservation reservation = sharingCar.getReservations().get(0);
        CarReservationStatus status = reservation.getStatus();
        assertThat(status).isEqualTo(CarReservationStatus.RESERVED);
    }

    @Test
    public void 예약_상태_정렬_확인() {
        // given
        Long sharingZoneId = 2L;
        LocalDateTime now = LocalDateTime.now();
        // when
        List<SharingCar> reservatableCar = sharingCarRepository.findReservatableCar(sharingZoneId,
            now);
        // then
        // 2L -> RESERVED, 4L -> WAITING
        SharingCar sharingCar = reservatableCar.get(1);
        Long id = sharingCar.getId();
        assertThat(id).isEqualTo(2L);
    }

    @Test
    public void 차량_타입_정렬() {
        // given
        Long sharingZoneId = 3L;
        LocalDateTime now = LocalDateTime.now();
        // when
        List<SharingCar> reservatableCar = sharingCarRepository.findReservatableCar(sharingZoneId,
            now);
        // then
        // 0 LIGHT_CAR, 1 SEMI_MIDSIZE_CAR, 2 MIDSIZE_CAR, 3 LARGE_CAR,
        SharingCar sharingCar = reservatableCar.get(1);
        StandardCar standardCar = sharingCar.getStandardCar();
        CarType type = standardCar.getType();
        assertThat(type).isEqualTo(CarType.SEMI_MIDSIZE_CAR);
    }

}