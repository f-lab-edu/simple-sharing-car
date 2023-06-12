package com.flab.simplesharingcar.service.sharing;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
class SharingCarServiceTest {

    @Autowired
    private SharingCarRepository sharingCarRepository;

    private SharingCarService sharingCarService;

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        sharingCarService = new SharingCarService(sharingCarRepository);
    }

    @Test
    public void 차량_예약_현재_상태_조회() {
        // given
        Long sharingZoneId = 1L;
        LocalDateTime now = LocalDateTime.now();
        // when
        List<SharingCar> result = sharingCarService.findByZoneIdAndTime(
            sharingZoneId, now);
        // then
        SharingCar sharingCar = result.get(0);
        CarReservationStatus status = sharingCar.getStatus();
        assertThat(status).isEqualTo(CarReservationStatus.RESERVED);
    }
}