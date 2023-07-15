package com.flab.simplesharingcar.dto;

import com.flab.simplesharingcar.constants.CarType;
import com.flab.simplesharingcar.constants.ReservationStatus;
import com.flab.simplesharingcar.domain.ReservationTime;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.StandardCar;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class CarSearchResult {

    private Long id;
    private String type;
    private String model;
    private String status;
    private Integer price;

    @QueryProjection
    public CarSearchResult(SharingCar sharingCar, LocalDateTime startTime, LocalDateTime endTime,
        ReservationStatus status) {
        StandardCar standardCar = sharingCar.getStandardCar();
        CarType carType = standardCar.getType();
        String model = standardCar.getModel();
        ReservationTime reservationTime = new ReservationTime(startTime, endTime);
        if (status == null) {
            status = ReservationStatus.WAITING;
        }

        this.id = sharingCar.getId();
        this.type = carType.getName();
        this.model = model;
        this.status = status.getName();
        this.price = sharingCar.calculatePriceByTime(reservationTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarSearchResult)) {
            return false;
        }
        CarSearchResult that = (CarSearchResult) o;
        return getId().equals(that.getId())
            && Objects.equals(getType(), that.getType())
            && Objects.equals(getModel(), that.getModel())
            && Objects.equals(getStatus(), that.getStatus())
            && Objects.equals(getPrice(), that.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
