package com.gomson.tryangle.dto;

import com.gomson.tryangle.domain.*;
import lombok.Getter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class GuideImageListDTO {

    private GuideDTO guideDTO;
    private List<String> guideImageList;

    public GuideImageListDTO(GuideDTO guideDTO, List<String> guideImageList) {
        this.guideDTO = guideDTO;
        this.guideImageList = guideImageList;
    }
}
