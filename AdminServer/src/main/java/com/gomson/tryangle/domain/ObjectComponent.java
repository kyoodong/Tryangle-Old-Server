package com.gomson.tryangle.domain;

import lombok.Getter;

@Getter
public class ObjectComponent extends Component {

    private int clazz;
    private int centerPointX;
    private int centerPointY;
    private float area;

    public ObjectComponent(long id, long componentId, int clazz, int centerPointX, int centerPointY, float area) {
        super(id, componentId);
        this.clazz = clazz;
        this.centerPointX = centerPointX;
        this.centerPointY = centerPointY;
        this.area = area;
    }
}
