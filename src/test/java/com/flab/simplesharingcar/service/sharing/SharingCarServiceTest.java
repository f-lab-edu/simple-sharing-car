package com.flab.simplesharingcar.service.sharing;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.simplesharingcar.config.QuerydslConfig;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.repository.SharingCarRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUtil;
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
    public void 차량_예약_정보_안가져오게() {
        // given
        Long sharingZoneId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plus = now.plus(2, ChronoUnit.HOURS);
        ReservationTime reservationTime = new ReservationTime(now, plus);
        // when
        List<SharingCar> result = sharingCarService.findByZoneIdAndTime(
            sharingZoneId, reservationTime);
        // then
        SharingCar sharingCar = result.get(0);
        assertThat(persistenceUtil.isLoaded(sharingCar.getReservations())).isFalse();
    }
}