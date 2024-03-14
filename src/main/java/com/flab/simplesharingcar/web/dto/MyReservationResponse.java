package com.flab.simplesharingcar.web.dto;

import com.flab.simplesharingcar.dto.MyReservationSearchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyReservationResponse {

    private Slice<MyReservationSearchResult> slice;


}
