package com.flab.simplesharingcar.vo;

import lombok.Data;

@Data
public class SharingCarVo {

    private Long id;

    private SharingZoneVo sharingZoneVo;

    private StandardCarVo standardCarVo;

}
