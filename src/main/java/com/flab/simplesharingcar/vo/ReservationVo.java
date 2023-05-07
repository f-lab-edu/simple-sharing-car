package com.flab.simplesharingcar.vo;

import com.flab.simplesharingcar.constants.ReservationStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReservationVo {

    private Long id;

    private UserVo user;

    private SharingCarVo sharingCarVo;

    private LocalDateTime resStartTime;

    private LocalDateTime resEndTime;

    private ReservationStatus status;
}
