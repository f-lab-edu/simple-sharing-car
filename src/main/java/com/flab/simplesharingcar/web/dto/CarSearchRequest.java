package com.flab.simplesharingcar.web.dto;

import com.flab.simplesharingcar.annotation.StartAndEndTimeCheck;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@StartAndEndTimeCheck(startTime = "resStartTime", endTime = "resEndTime", message = "시작 시간이 종료 시간보다 늦을 수 없습니다.")
public class CarSearchRequest {

    @NotNull(message = "주차장 ID는 필수 입니다.")
    private Long sharingZoneId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "시작 시간은 필수 입니다.")
    private LocalDateTime resStartTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "종료 시간은 필수 입니다.")
    private LocalDateTime resEndTime;

}
