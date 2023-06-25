package com.flab.simplesharingcar.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTime {

    @Column(name = "resStartTime")
    private LocalDateTime startTime;

    @Column(name = "resEndTime")
    private LocalDateTime endTime;

    public ReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
    }

    private void setStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("시작 시간은 필수 입니다.");
        }
        this.startTime = startTime;
    }

    private void setEndTime(LocalDateTime endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("종료 시간은 필수 입니다.");
        }
        this.endTime = endTime;
    }

    public void validateReservationTime() {
        if (!isStartTimeFasterThanEndTime()) {
            throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }
    }

    public boolean isStartTimeFasterThanEndTime() {
        return !startTime.isAfter(endTime);
    }

}
