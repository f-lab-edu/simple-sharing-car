package com.flab.simplesharingcar.constants;

import lombok.Getter;

@Getter
public enum CarType {
    LIGHT_CAR(1, "경차"),
    SEMI_MIDSIZE_CAR(2, "준중형"),
    MIDSIZE_CAR(3, "중형"),
    LARGE_CAR(4, "대형");

    Integer order;
    
    String name;

    CarType(Integer order, String name) {
        this.order = order;
        this.name = name;
    }
}
