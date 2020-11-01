package com.gomson.tryangle.domain.component;

import com.gomson.tryangle.domain.Guide;
import com.gomson.tryangle.domain.Point;

import java.util.ArrayList;

public class LineComponent extends Component {

    private Point start;
    private Point end;

    public LineComponent(long id, long componentId, ArrayList<Guide> guideList, Point start, Point end) {
        super(id, componentId, guideList);
        this.start = start;
        this.end = end;
    }
}
