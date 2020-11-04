package com.gomson.tryangle.domain.component;

import com.gomson.tryangle.domain.guide.Guide;
import com.gomson.tryangle.domain.Point;
import com.gomson.tryangle.domain.Roi;
import com.gomson.tryangle.domain.guide.ObjectGuide;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;

@Getter
public class ObjectComponent extends Component {

    private int clazz;
    private Point centerPoint;
    private float area;
    private String roiStr;
    private Roi roi;

    public ObjectComponent() {
        super(0, 0, new ArrayList<>());
    }

    public ObjectComponent(long id, long componentId, ArrayList<ObjectGuide> guideList, int clazz, Point centerPoint, float area,
                           String roiStr) {
        super(id, componentId, guideList);
        this.clazz = clazz;
        this.centerPoint = centerPoint;
        this.area = area;
        setRoiStr(roiStr);
    }

    public ObjectComponent(JSONObject json) {
        super(0, 0, new ArrayList<>());

        try {
            this.clazz = json.getInt("class_ids");
            setRoiStr(json.getString("rois"));
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

    @Override
    public ArrayList<ObjectGuide> getGuideList() {
        ArrayList<ObjectGuide> guides = new ArrayList<>();
        for (Guide guide : guideList) {
            guides.add((ObjectGuide) guide);
        }
        return guides;
    }
}
