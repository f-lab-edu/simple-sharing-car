package com.flab.simplesharingcar.web.dto;

import com.flab.simplesharingcar.constants.CarStatus;
import com.flab.simplesharingcar.domain.SharingCar;
import com.flab.simplesharingcar.domain.StandardCar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarSearchResult {

    private Long id;

    private String type;

    private String model;

    private String status;

    private String typeName;

    public static CarSearchResult from(SharingCar sharingCar) {
        Long id = sharingCar.getId();
        StandardCar standardCar = sharingCar.getStandardCar();
        CarStatus carStatus = sharingCar.getStatus();
        String status = carStatus.toString();

        String type = "";
        String model = "";

        if (standardCar != null) {
            type = standardCar.getType().toString();
            model = standardCar.getModel();
        }
        return CarSearchResult.builder()
            .id(id)
            .type(type)
            .model(model)
            .status(status)
            .build();
    }
}
