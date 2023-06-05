package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.domain.SharingZone;
import com.flab.simplesharingcar.service.sharing.SharingZoneService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final SharingZoneService sharingZoneService;

    @GetMapping
    public String home() {
        return "home";
    }

    @PostMapping("/testSave")
    public ResponseEntity<SharingZone> save(@RequestBody SharingZone sharingZone) {
        return ResponseEntity.ok(sharingZoneService.save(sharingZone));
    }

    @GetMapping("/testSearch")
    public ResponseEntity<List<SharingZone>> search(
        @RequestParam("lng") Double lng
        , @RequestParam("lat") Double lat
        , @RequestParam("km") Double km
    ) {
        return ResponseEntity.ok(sharingZoneService.findByLocation(lat, lng, km));
    }
}
