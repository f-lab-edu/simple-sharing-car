package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.domain.SharingZone;
import com.flab.simplesharingcar.service.sharing.SharingZoneService;
import com.flab.simplesharingcar.web.dto.SharingZoneRequest;
import com.flab.simplesharingcar.web.dto.SharingZoneResponse;
import com.flab.simplesharingcar.web.dto.SharingZoneSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/zone")
public class SharingZoneController {

    private final SharingZoneService sharingZoneService;

    @GetMapping("/map")
    public String viewMap() {
        return "viewZoneMap";
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<SharingZoneResponse> find(@Valid SharingZoneRequest request) {
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();
        Double distance = request.getDistance();

        List<SharingZone> sharingZones = sharingZoneService.findByLocation(latitude, longitude,
            distance);
        List<SharingZoneSearchResult> results = sharingZones.stream()
                .map(SharingZoneSearchResult::from)
                .collect(Collectors.toList());

        SharingZoneResponse responseBody = new SharingZoneResponse(results);
        return ResponseEntity.ok(responseBody);
    }

}
