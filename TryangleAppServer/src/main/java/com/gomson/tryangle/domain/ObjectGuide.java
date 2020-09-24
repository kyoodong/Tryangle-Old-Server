package com.gomson.tryangle.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

@Getter
@Setter
public class ObjectGuide extends Guide {

    private int diffX;
    private int diffY;
    private int objectClass;

    public ObjectGuide(int objectId, int guideId, int diffX, int diffY, int objectClass) {
        super(objectId, guideId);
        this.diffX = diffX;
        this.diffY = diffY;
        this.objectClass = objectClass;
    }
}
