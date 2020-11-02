package com.gomson.tryangle.domain.component;

import com.gomson.tryangle.domain.guide.Guide;
import com.gomson.tryangle.domain.Point;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Map;

@Getter
public class PersonComponent extends ObjectComponent {

    private int pose;
    private Map<String, Point> posePoints;

    public PersonComponent(long id, long componentId, ArrayList<? extends Guide> guideList, int clazz, Point center, float area,
                           String mask, String roi, int pose, Map<String, Point> posePoints) {
        super(id, componentId, guideList, clazz, center, area, mask, roi);
        this.pose = pose;
        this.posePoints = posePoints;
    }
}
