package com.flab.simplesharingcar.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharingZoneResponse {

    private List<SharingZoneSearchResult> sharingZoneList = new ArrayList<>();

}
