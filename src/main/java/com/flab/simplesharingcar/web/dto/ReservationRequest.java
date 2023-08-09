package com.flab.simplesharingcar.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.flab.simplesharingcar.annotation.StartAndEndTimeCheck;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@StartAndEndTimeCheck(startTime = "resStartTime", endTime = "resEndTime", message = "시작 시간이 종료 시간보다 늦을 수 없습니다.")
public class ReservationRequest {

    @NotNull(message = "결제 정보는 필수 입니다.")
    private Long paymentId;

    @NotNull(message = "예약 차량 정보는 필수 입니다.")
    private Long sharingCarId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "예약 시작 시간은 필수 입니다.")
    private LocalDateTime resStartTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "예약 종료 시간은 필수 입니다.")
    private LocalDateTime resEndTime;

}
