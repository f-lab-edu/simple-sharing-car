package com.flab.simplesharingcar.constants;

import lombok.Getter;

@Getter
public enum CarType {
    LIGHT_CAR(1), SEMI_MIDSIZE_CAR(2), MIDSIZE_CAR(3), LARGE_CAR(4);

    Integer order;

    CarType(Integer order) {
        this.order = order;
    }
}
