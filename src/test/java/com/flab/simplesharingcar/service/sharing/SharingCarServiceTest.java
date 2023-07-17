package com.flab.simplesharingcar.service.sharing;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.dto.CarSearchResult;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUtil;
import org.junit.jupiter.api.Assertions;
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

    @PersistenceContext
    EntityManager entityManager;

    PersistenceUtil persistenceUtil;

    private SharingCarService sharingCarService;

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        sharingCarService = new SharingCarService(sharingCarRepository);
        persistenceUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
    }

    @Test
    public void 차량_검색_확인() {
        // given
        Long sharingZoneId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plus = now.plus(2, ChronoUnit.HOURS);
        ReservationTime reservationTime = new ReservationTime(now, plus);
        // when
        List<CarSearchResult> result = sharingCarService.findByZoneIdAndTime(
            sharingZoneId, reservationTime);
        // then
        CarSearchResult sharingCar = result.get(0);
        assertThat(sharingCar).isNotNull();
    }

    @Test
    public void 차량_예약_가격_확인() {
        // given
        Long sharingZoneId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plus = now.plus(2, ChronoUnit.HOURS);
        ReservationTime reservationTime = new ReservationTime(now, plus);
        // when
        List<CarSearchResult> result = sharingCarService.findByZoneIdAndTime(
            sharingZoneId, reservationTime);
        // then
        // 그랜저 분당 70원 * 120분
        CarSearchResult sharingCar = result.get(0);
        assertThat(sharingCar.getPrice()).isEqualTo(8400);
    }
}