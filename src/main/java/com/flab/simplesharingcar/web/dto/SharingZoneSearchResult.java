package com.flab.simplesharingcar.web.dto;

import com.flab.simplesharingcar.domain.Location;
import com.flab.simplesharingcar.domain.SharingZone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharingZoneSearchResult {

    private String name;
    private Double latitude;
    private Double longitude;
    public static SharingZoneSearchResult from(SharingZone sharingZone) {
        String name = sharingZone.getName();
        Location location = sharingZone.getLocation();
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        return SharingZoneSearchResult.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}
