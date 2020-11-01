package com.gomson.tryangle.dto;

import com.gomson.tryangle.domain.*;
import com.gomson.tryangle.domain.component.Component;
import com.gomson.tryangle.domain.component.LineComponent;
import com.gomson.tryangle.domain.component.ObjectComponent;
import com.gomson.tryangle.domain.component.PersonComponent;
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

    private static final String[] BodyPart = {
        "NOSE",
        "LEFT_EYE",
        "RIGHT_EYE",
        "LEFT_EAR",
        "RIGHT_EAR",
        "LEFT_SHOULDER",
        "RIGHT_SHOULDER",
        "LEFT_ELBOW",
        "RIGHT_ELBOW",
        "LEFT_WRIST",
        "RIGHT_WRIST",
        "LEFT_HIP",
        "RIGHT_HIP",
        "LEFT_KNEE",
        "RIGHT_KNEE",
        "LEFT_ANKLE",
        "RIGHT_ANKLE"
    };

    private List<Component> componentList;
    private List<Integer> dominantColorList;
    private int cluster = -1;

    public GuideDTO(JSONObject jsonObject) {
        componentList = new ArrayList<>();
        dominantColorList = new ArrayList<>();

        try {
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
                    componentList.add(new LineComponent(0, line.getInt("id"),
                            null,
                            new Point(startX, startY),
                            new Point(endX, endY)));

                } else if (component.has("ObjectComponent")) {
                    JSONObject object = component.getJSONObject("ObjectComponent");
                    ObjectComponent objectComponent;

                    int id = object.getInt("id");
                    int clazz = object.getInt("class");
                    int centerPointX = object.getInt("center_point_x");
                    int centerPointY = object.getInt("center_point_y");
                    int area = object.getInt("area");
                    String mask = object.getString("mask");
                    mask = mask.replaceAll("true", "1")
                            .replaceAll("false", "0")
                            .replaceAll(" ", "");

                    String roi = object.getString("roi");

                    if (object.has("pose")) {
                        int pose = object.getInt("pose");
                        Map<String, Point> posePoints = new HashMap<>();

                        JSONArray posePointArray = object.getJSONArray("pose_points");
                        for (int j = 0; j < posePointArray.length(); j++) {
                            if (posePointArray.getString(j).equals("None")) {
                                posePoints.put(BodyPart[j], new Point(-1, -1));
                                continue;
                            }

                            int x = posePointArray.getJSONArray(j).getInt(0);
                            int y = posePointArray.getJSONArray(j).getInt(1);
                            posePoints.put(BodyPart[j], new Point(x, y));
                        }

                        objectComponent = new PersonComponent(
                                0,
                                id,
                                null,
                                clazz,
                                new Point(centerPointX, centerPointY),
                                (float) (imageWidth * imageHeight) / area,
                                mask,
                                roi,
                                pose,
                                posePoints
                        );
                    } else {
                        objectComponent = new ObjectComponent(
                                0,
                                id,
                                null,
                                clazz,
                                new Point(centerPointX, centerPointY),
                                (float) (imageWidth * imageHeight) / area,
                                mask,
                                roi
                        );
                    }
                    componentList.add(objectComponent);
                }
            }

            JSONArray dominantColorArray = jsonObject.getJSONArray("dominant_color_list");
            for (int i = 0; i < dominantColorArray.length(); i++) {
                dominantColorList.add(dominantColorArray.getInt(i));
            }

            int cluster = jsonObject.getInt("cluster");
            this.cluster = cluster;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        int count = 0;
        for (Component component : componentList) {
            count += component.getGuideList().size();
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
