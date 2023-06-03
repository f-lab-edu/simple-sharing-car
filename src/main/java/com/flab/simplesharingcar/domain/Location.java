package com.flab.simplesharingcar.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Location {

    @Column(columnDefinition="decimal")
    private Double latitude;

    @Column(columnDefinition="decimal")
    private Double longitude;

}
