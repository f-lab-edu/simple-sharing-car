package com.flab.simplesharingcar.dto;

import com.flab.simplesharingcar.constants.CarReservationStatus;
import com.flab.simplesharingcar.constants.CarType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MyReservationSearchResult {

    private Long reservationId;
    private String carType;
    private String carModel;
    private String status;
    private Integer price;

    @QueryProjection
    public MyReservationSearchResult(Long reservationId, CarType carType, String carModel, CarReservationStatus status, Integer price) {
        this.reservationId = reservationId;
        this.carType = carType.getName();
        this.carModel = carModel;
        this.status = status.getName();
        this.price = price;
    }

}
