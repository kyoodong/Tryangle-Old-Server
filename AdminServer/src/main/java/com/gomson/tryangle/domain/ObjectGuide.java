package com.gomson.tryangle.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

@Getter
@Setter
public class ObjectGuide extends Guide {

    private int diffX;
    private int diffY;

    public ObjectGuide(int objectId, int guideId, int diffX, int diffY) {
        super(objectId, guideId);
        this.diffX = diffX;
        this.diffY = diffY;
    }
}
