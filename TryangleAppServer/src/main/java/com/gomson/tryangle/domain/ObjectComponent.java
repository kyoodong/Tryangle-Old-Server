package com.gomson.tryangle.domain;

import lombok.Getter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObjectComponent extends Component {

    private int clazz;
    private int centerPointX;
    private int centerPointY;
    private float area;
    private String mask;
    private String roi;
    private List<List<Integer>> maskList;
    private List<Integer> roiList;

    public ObjectComponent(long id, long componentId, int clazz, int centerPointX, int centerPointY, float area,
                           String mask, String roi) {
        super(id, componentId);
        this.clazz = clazz;
        this.centerPointX = centerPointX;
        this.centerPointY = centerPointY;
        this.area = area;
        setMask(mask);
        setRoi(roi);
    }

    public ObjectComponent(JSONObject json) {
        super(0, 0);

        try {
            this.clazz = json.getInt("class_ids");
            setMask(json.getString("mask"));
            setRoi(json.getString("rois"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMask(String mask) {
        this.mask = mask;

        try {
            this.maskList = new ArrayList<>();
            JSONArray array = new JSONArray(mask);
            for (int i = 0; i < array.length(); i++) {
                JSONArray arr = array.getJSONArray(i);
                this.maskList.add(new ArrayList<>());
                for (int j = 0; j < arr.length(); j++) {
                    Object obj = arr.get(i);
                    if (obj instanceof Integer) {
                        this.maskList.get(i).add((Integer) obj);
                    } else {
                        Boolean b = (Boolean) obj;
                        this.maskList.get(i).add(b ? 1 : 0);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setRoi(String roi) {
        this.roi = roi;

        try {
            this.roiList = new ArrayList<>();
            JSONArray array = new JSONArray(roi);
            for (int i = 0; i < array.length(); i++) {
                this.roiList.add(array.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
