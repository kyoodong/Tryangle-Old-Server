package com.gomson.tryangle.dto;

import com.gomson.tryangle.domain.*;
import com.gomson.tryangle.domain.component.LineComponent;
import com.gomson.tryangle.domain.component.ObjectComponent;
import com.gomson.tryangle.domain.component.PersonComponent;
import com.gomson.tryangle.domain.guide.ObjectGuide;
import lombok.Getter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.*;

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

    private List<LineComponent> lineComponentList;
    private List<ObjectComponent> objectComponentList;
    private List<PersonComponent> personComponentList;
    private List<Integer> dominantColorList;
    private MaskList mask;
    private int cluster = -1;
    private Integer background;

    public GuideDTO(JSONObject jsonObject) {
        lineComponentList = new ArrayList<>();
        personComponentList = new ArrayList<>();
        objectComponentList = new ArrayList<>();
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
                    lineComponentList.add(new LineComponent(0, line.getInt("id"),
                            null,
                            new Point(startX, startY),
                            new Point(endX, endY)));

                } else if (component.has("ObjectComponent")) {
                    JSONObject object = component.getJSONObject("ObjectComponent");
                    int id = object.getInt("id");
                    int clazz = object.getInt("class");
                    int centerPointX = object.getInt("center_point_x");
                    int centerPointY = object.getInt("center_point_y");
                    int area = object.getInt("area");
                    String roi = object.getString("roi");

                    ArrayList<ObjectGuide> guideList = new ArrayList<>();
                    if (object.has("guide_list")) {
                        JSONArray guideJsonList = object.getJSONArray("guide_list");
                        for (int j = 0; j < guideJsonList.length(); j++) {
                            ObjectGuide objectGuide = new ObjectGuide(guideJsonList.getJSONObject(j).getJSONObject("ObjectGuide"));
                            guideList.add(objectGuide);
                        }
                    }

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

                        PersonComponent personComponent = new PersonComponent(
                                0,
                                id,
                                guideList,
                                clazz,
                                new Point(centerPointX, centerPointY),
                                (float) (imageWidth * imageHeight) / area,
                                roi,
                                pose,
                                posePoints
                        );
                        personComponentList.add(personComponent);
                    } else {
                        ObjectComponent objectComponent = new ObjectComponent(
                                0,
                                id,
                                guideList,
                                clazz,
                                new Point(centerPointX, centerPointY),
                                (float) (imageWidth * imageHeight) / area,
                                roi
                        );
                        objectComponentList.add(objectComponent);
                    }
                }
            }

            JSONArray dominantColorArray = jsonObject.getJSONArray("dominant_color_list");
            for (int i = 0; i < dominantColorArray.length(); i++) {
                dominantColorList.add(dominantColorArray.getInt(i));
            }

            JSONArray maskArray = jsonObject.getJSONArray("mask");
            setMaskJson(maskArray);

            int cluster = jsonObject.getInt("cluster");
            this.cluster = cluster;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Integer> getObjectClassCount() {
        Map<Integer, Integer> map = new HashMap<>();
        for (ObjectComponent objectComponent : objectComponentList) {
            if (map.containsKey(objectComponent.getClazz())) {
                map.put(objectComponent.getClazz(), map.get(objectComponent.getClazz()) + 1);
            } else {
                map.put(objectComponent.getClazz(), 1);
            }
        }
        return map;
    }

    public void setMaskJson(JSONArray maskArray) throws JSONException {
        this.mask = new MaskList();
        for (int i = 0; i < maskArray.length(); i++) {
            JSONArray arr = maskArray.getJSONArray(i);
            this.mask.add(new byte[arr.length()]);
            for (int j = 0; j < arr.length(); j++) {
                int value = arr.getInt(j);
                this.mask.get(i)[j] = (byte) value;
            }
        }
    }
}
