package com.gomson.tryangle.domain.component;

import com.gomson.tryangle.domain.guide.Guide;
import com.gomson.tryangle.domain.Point;
import com.gomson.tryangle.domain.Roi;
import lombok.Getter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;

@Getter
public class ObjectComponent extends Component {

    private int clazz;
    private Point centerPoint;
    private float area;
    private String maskStr;
    private String roiStr;
    private ArrayList<ArrayList<Integer>> mask;
    private Roi roi;

    public ObjectComponent(long id, long componentId, ArrayList<? extends Guide> guideList, int clazz, Point centerPoint, float area,
                           String maskStr, String roiStr) {
        super(id, componentId, guideList);
        this.clazz = clazz;
        this.centerPoint = centerPoint;
        this.area = area;
        setMaskStr(maskStr);
        setRoiStr(roiStr);
    }

    public ObjectComponent(JSONObject json) {
        super(0, 0, new ArrayList<>());

        try {
            this.clazz = json.getInt("class_ids");
            setMaskStr(json.getString("mask"));
            setRoiStr(json.getString("rois"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMaskStr(String maskStr) {
        this.maskStr = maskStr;

        try {
            this.mask = new ArrayList<>();
            JSONArray array = new JSONArray(maskStr);
            for (int i = 0; i < array.length(); i++) {
                JSONArray arr = array.getJSONArray(i);
                this.mask.add(new ArrayList<>());
                for (int j = 0; j < arr.length(); j++) {
                    Object obj = arr.get(j);
                    if (obj instanceof Integer) {
                        this.mask.get(i).add((Integer) obj);
                    } else {
                        Boolean b = (Boolean) obj;
                        this.mask.get(i).add(b ? 1 : 0);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setRoiStr(String roiStr) {
        this.roiStr = roiStr;

        try {
            JSONArray array = new JSONArray(roiStr);
            this.roi = new Roi(array.getInt(1), array.getInt(3), array.getInt(0), array.getInt(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
