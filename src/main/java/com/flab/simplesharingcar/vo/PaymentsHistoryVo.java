package com.flab.simplesharingcar.vo;

import lombok.Data;

@Data
public class PaymentsHistoryVo {

    private Long id;

    private ReservationVo reservationVo;

    private Integer price;
}
