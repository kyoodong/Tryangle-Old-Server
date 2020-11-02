package com.gomson.tryangle.domain.guide;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@Getter
@Setter
public class ObjectGuide extends Guide {

    private int diffX;
    private int diffY;
    private int objectClass;

    public ObjectGuide(int guideId, int diffX, int diffY, int objectClass) {
        super(guideId);
        this.diffX = diffX;
        this.diffY = diffY;
        this.objectClass = objectClass;
    }

    public ObjectGuide(JSONObject json) {
        super(0);
        try {
            int guideId = json.getInt("guide_id");
            int diffX = json.getInt("diff_x");
            int diffY = json.getInt("diff_y");

            setGuideId(guideId);
            setDiffX(diffX);
            setDiffY(diffY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
