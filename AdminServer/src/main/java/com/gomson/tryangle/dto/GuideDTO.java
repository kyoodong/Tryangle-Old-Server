package com.gomson.tryangle.dto;

import com.gomson.tryangle.domain.Guide;
import com.gomson.tryangle.domain.LineGuide;
import com.gomson.tryangle.domain.ObjectGuide;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GuideDTO {

    private List<List<Guide>> guideList;

    public GuideDTO(JSONObject jsonObject) {
        guideList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++)
            guideList.add(new ArrayList<>());

        try {
            JSONArray data = jsonObject.getJSONArray("guide");
            JSONArray guideArray = data.getJSONArray(0);
            for (int i = 0; i < guideArray.length(); i++) {
                JSONObject guideObject = guideArray.getJSONObject(i);
                if (guideObject.has("LineGuide")) {
                    JSONObject lineGuide = guideObject.getJSONObject("LineGuide");
                    int objectId = lineGuide.getInt("object_id");
                    int guideId = lineGuide.getInt("guide_id");
                    guideList.get(i).add(new LineGuide(objectId, guideId));
                } else if (guideObject.has("ObjectGuide")) {
                    JSONObject objectGuide = guideObject.getJSONObject("ObjectGuide");
                    int objectId = objectGuide.getInt("object_id");
                    int guideId = objectGuide.getInt("guide_id");
                    int diffX = objectGuide.getInt("diff_x");
                    int diffY = objectGuide.getInt("diff_y");
                    guideList.get(i).add(new ObjectGuide(objectId, guideId, diffX, diffY));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        int count = 0;
        for (List<Guide> list : guideList) {
            count += list.size();
        }
        return count;
    }
}
