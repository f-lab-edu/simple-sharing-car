package com.flab.simplesharingcar.web.dto;

import com.flab.simplesharingcar.domain.SharingZone;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharingZoneResponse {

    @Builder.Default
    List<SharingZone> sharingZoneList = new ArrayList<>();

}
