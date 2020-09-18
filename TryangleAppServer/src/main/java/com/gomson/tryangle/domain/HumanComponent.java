package com.gomson.tryangle.domain;

import lombok.Getter;

@Getter
public class HumanComponent extends ObjectComponent {

    private int pose;

    public HumanComponent(long id, long componentId, int clazz, int centerPointX, int centerPointY, float area, int pose) {
        super(id, componentId, clazz, centerPointX, centerPointY, area);
        this.pose = pose;
    }
}
