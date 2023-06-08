package com.flab.simplesharingcar.web.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SharingZoneRequest {

    @NotNull(message = "위도 값은 필수 입니다.")
    @DecimalMin(value = "-90.0", message = "위도 값이 범위에 벗어 났습니다.")
    @DecimalMax(value = "90.0", message = "위도 값이 범위에 벗어 났습니다.")
    private Double latitude;

    @NotNull(message = "경도 값은 필수 입니다.")
    @DecimalMin(value = "-180.0", message = "경도 값이 범위에 벗어 났습니다.")
    @DecimalMax(value = "180.0", message = "경도 값이 범위에 벗어 났습니다.")
    private Double longitude;

    @NotNull(message = "거리는 필수 입니다.")
    private Double distance;

}
