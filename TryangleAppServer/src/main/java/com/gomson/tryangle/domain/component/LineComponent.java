package com.gomson.tryangle.domain.component;

import com.gomson.tryangle.domain.Point;
import com.gomson.tryangle.domain.guide.Guide;
import com.gomson.tryangle.domain.guide.LineGuide;

import java.util.ArrayList;

public class LineComponent extends Component {

    private Point start;
    private Point end;

    public LineComponent(long id, long componentId, ArrayList<LineGuide> guideList, Point start, Point end) {
        super(id, componentId, guideList);
        this.start = start;
        this.end = end;
    }

    @Override
    public ArrayList<LineGuide> getGuideList() {
        ArrayList<LineGuide> guides = new ArrayList<>();
        for (Guide guide : guideList) {
            guides.add((LineGuide) guide);
        }
        return guides;
    }
}
