package com.flab.simplesharingcar.service.sharing;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.simplesharingcar.config.RedisConfig;
import com.flab.simplesharingcar.domain.Location;
import com.flab.simplesharingcar.domain.SharingZone;
import com.flab.simplesharingcar.repository.SharingZoneRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Import({RedisConfig.class})
class SharingZoneServiceTest {

    @Autowired
    private GeoOperations<String, Object> geoOperations;

    @Autowired
    private SharingZoneRepository sharingZoneRepository;

    private SharingZoneService sharingZoneService;

    private final String redis_key = "sharing_zone";

    @BeforeTestClass
    @Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
    void initClass() {
    }

    @BeforeEach
    void init() {
        sharingZoneService = new SharingZoneService(geoOperations,
            sharingZoneRepository);
        List<SharingZone> findAll = sharingZoneRepository.findAll();
        findAll.stream()
            .forEach(sharingZone -> {
                Point pointByLocation = sharingZone.getPointByZoneLocation();
                geoOperations.add(redis_key, pointByLocation, sharingZone);
            });
    }

    @Test
    public void 저장_테스트() {
        // given
        Double latitude = 126.93312079932643;
        Double longitude = 37.35805178764877;
        Location location = new Location(latitude, longitude);

        SharingZone saveZone = SharingZone.builder()
            .name("산본역 주차장")
            .location(location)
            .build();
        // when
        sharingZoneService.save(saveZone);
        // then
        List<String> sharing_zone = geoOperations.hash(redis_key, saveZone);
        assertThat(sharing_zone).hasSizeGreaterThan(0);
    }

    @Test
    public void 일키로내_주차장_찾기() {
        // given
        Double latitude = 126.0;
        Double longitude = 37.12;
        Double km = 1.0;

        // when
        List<SharingZone> byLocation = sharingZoneService.findByLocation(latitude, longitude, km);

        // then
        assertThat(byLocation).hasSize(4);
        byLocation.stream()
            .map(SharingZone::getName)
            .forEach(System.out::println);
    }
}