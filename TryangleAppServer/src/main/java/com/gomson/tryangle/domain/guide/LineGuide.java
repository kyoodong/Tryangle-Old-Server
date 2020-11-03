package com.gomson.tryangle.domain.guide;

import com.gomson.tryangle.domain.guide.Guide;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineGuide extends Guide {

    public LineGuide(int guideId) {
        super(guideId);
    }
}
