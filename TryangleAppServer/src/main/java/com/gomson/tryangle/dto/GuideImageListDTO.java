package com.gomson.tryangle.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GuideImageListDTO {

    private GuideDTO guideDTO;
    private List<String> guideImageList;

    public GuideImageListDTO(GuideDTO guideDTO, List<String> guideImageList) {
        this.guideDTO = guideDTO;
        this.guideImageList = guideImageList;
    }
}
