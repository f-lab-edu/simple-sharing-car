package com.flab.simplesharingcar.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.flab.simplesharingcar.domain.Reservation;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

    private Long reservationId;

    private Long userId;

    private Long sharingCarId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime resStartTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime resEndTime;

    public static ReservationResponse from(Reservation reservation) {
        Long id = reservation.getId();

        SharingCar sharingCar = reservation.getSharingCar();
        Long sharingCarId = sharingCar.getId();

        User user = reservation.getUser();
        Long userId = user.getId();

        ReservationTime reservationTime = reservation.getReservationTime();
        LocalDateTime startTime = reservationTime.getStartTime();
        LocalDateTime endTime = reservationTime.getEndTime();

        ReservationResponse result = ReservationResponse.builder()
            .reservationId(id)
            .userId(userId)
            .sharingCarId(sharingCarId)
            .resStartTime(startTime)
            .resEndTime(endTime)
            .build();
        return result;
    }
}
