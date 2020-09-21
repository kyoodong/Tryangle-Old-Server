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
public class GuideDTO {

    private List<List<Guide>> guideList;
    private List<Component> componentList;
    private List<Integer> dominantColorList;

    public GuideDTO(JSONObject jsonObject) {
        guideList = new ArrayList<>(10);
        componentList = new ArrayList<>();
        dominantColorList = new ArrayList<>();

        for (int i = 0; i < 10; i++)
            guideList.add(new ArrayList<>());

        try {
            JSONArray guide = jsonObject.getJSONArray("guide");
            JSONArray guideArray = guide.getJSONArray(0);
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
                    int objectClass = objectGuide.getInt("object_class");
                    guideList.get(i).add(new ObjectGuide(objectId, guideId, diffX, diffY, objectClass));
                }
            }

            JSONArray imageSize = jsonObject.getJSONArray("image_size");
            int imageHeight = imageSize.getInt(0);
            int imageWidth = imageSize.getInt(1);

            JSONArray componentArray = jsonObject.getJSONArray("component_list");
            for (int i = 0; i < componentArray.length(); i++) {
                JSONObject component = componentArray.getJSONObject(i);
                if (component.has("LineComponent")) {
                    JSONObject line = component.getJSONObject("LineComponent");
                    JSONArray linePointArray = line.getJSONArray("line");
                    int startX = linePointArray.getInt(0);
                    int startY = linePointArray.getInt(1);
                    int endX = linePointArray.getInt(2);
                    int endY = linePointArray.getInt(3);
                    componentList.add(new LineComponent(0, line.getInt("id"), startX, startY, endX, endY));

                } else if (component.has("ObjectComponent")) {
                    JSONObject object = component.getJSONObject("ObjectComponent");
                    ObjectComponent objectComponent;
                    if (object.has("pose")) {
                        objectComponent = new PersonComponent(
                                0,
                                object.getInt("id"),
                                object.getInt("class"),
                                object.getInt("center_point_x"),
                                object.getInt("center_point_y"),
                                (float) (imageWidth * imageHeight) / object.getInt("area"),
                                object.getInt("pose")
                        );
                    } else {
                        objectComponent = new ObjectComponent(
                                0,
                                object.getInt("id"),
                                object.getInt("class"),
                                object.getInt("center_point_x"),
                                object.getInt("center_point_y"),
                                (float) (imageWidth * imageHeight) / object.getInt("area")
                        );
                    }
                    componentList.add(objectComponent);
                }
            }

            JSONArray dominantColorArray = jsonObject.getJSONArray("dominant_color_list");
            for (int i = 0; i < dominantColorArray.length(); i++) {
                dominantColorList.add(dominantColorArray.getInt(i));
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

    public Map<Integer, Integer> getObjectClassCount() {
        Map<Integer, Integer> map = new HashMap<>();
        for (Component component : componentList) {
            if (component instanceof ObjectComponent) {
                ObjectComponent objectComponent = ((ObjectComponent) component);
                if (map.containsKey(objectComponent.getClazz())) {
                    map.put(objectComponent.getClazz(), map.get(objectComponent.getClazz()) + 1);
                } else {
                    map.put(objectComponent.getClazz(), 1);
                }
            }
        }
        return map;
    }
}
