package com.gomson.tryangle.domain;

public class LineComponent extends Component {

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public LineComponent(int id, int startX, int startY, int endX, int endY) {
        super(id);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
}
