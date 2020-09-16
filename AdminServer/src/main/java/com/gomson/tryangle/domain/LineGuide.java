package com.gomson.tryangle.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineGuide extends Guide {

    public LineGuide(int objectId, int guideId) {
        super(objectId, guideId);
    }
}
