package com.gomson.tryangle.domain.component;

import com.gomson.tryangle.domain.Point;

public class LineComponent extends Component {

    private Point start;
    private Point end;

    public LineComponent(long id, long componentId, Point start, Point end) {
        super(id, componentId);
        this.start = start;
        this.end = end;
    }
}
