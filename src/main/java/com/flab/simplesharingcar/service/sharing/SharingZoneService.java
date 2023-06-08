package com.flab.simplesharingcar.service.sharing;

import com.flab.simplesharingcar.domain.Location;
import com.flab.simplesharingcar.domain.SharingZone;
import com.flab.simplesharingcar.repository.SharingZoneRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SharingZoneService {

    private static final String SHARING_ZONE = "sharing_zone";

    private final RedisTemplate<String, Object> redisTemplate;

    private final SharingZoneRepository sharingZoneRepository;

    private final GeoOperations<String, Object> geoOperations;

    public SharingZoneService(RedisTemplate redisTemplate, SharingZoneRepository sharingZoneRepository) {
        this.redisTemplate = redisTemplate;
        this.sharingZoneRepository = sharingZoneRepository;
        this.geoOperations = redisTemplate.opsForGeo();
        initRedisData();
    }

    private void initRedisData() {
        redisTemplate.delete(SHARING_ZONE);

        List<SharingZone> allSharingZone = sharingZoneRepository.findAll();
        allSharingZone.stream()
            .forEach(sharingZone -> {
                Point pointByLocation = sharingZone.getPointByZoneLocation();
                geoOperations.add(SHARING_ZONE, pointByLocation, sharingZone);
            });
    }

    public SharingZone save(SharingZone sharingZone) {
        String name = sharingZone.getName();
        Location location = sharingZone.getLocation();

        SharingZone saveSharingZone = SharingZone.builder()
            .name(name)
            .location(location)
            .build();
        Point pointByLocation = saveSharingZone.getPointByZoneLocation();

        sharingZoneRepository.save(saveSharingZone);
        geoOperations.add(SHARING_ZONE, pointByLocation, saveSharingZone);

        return saveSharingZone;
    }

    public List<SharingZone> findByLocation(Double latitude, Double longitude, Double distance) {
        Point center = new Point(longitude, latitude);
        Distance distanceKm = new Distance(distance, Metrics.KILOMETERS);
        Circle inner = new Circle(center, distanceKm);

        GeoResults<GeoLocation<Object>> results = geoOperations.radius(SHARING_ZONE, inner);
        List<SharingZone> findZones = results.getContent().stream()
            .map(GeoResult::getContent)
            .map(GeoLocation::getName)
            .map(SharingZone.class::cast)
            .collect(Collectors.toList());
        return findZones;
    }
}
