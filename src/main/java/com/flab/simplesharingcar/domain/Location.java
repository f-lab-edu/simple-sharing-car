package com.flab.simplesharingcar.domain;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Location {

    private BigDecimal latitude;

    private BigDecimal longitude;

}
