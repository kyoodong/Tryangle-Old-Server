package com.gomson.tryangle.domain.component;

import com.gomson.tryangle.domain.guide.Guide;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Component {

    private long id;
    private long componentId;
    protected ArrayList<? extends Guide> guideList;

    public ArrayList<? extends Guide> getGuideList() {
        return new ArrayList<Guide>(guideList);
    }

    public void setGuideList(ArrayList<? extends Guide> guideList) {
        this.guideList = guideList;
    }
}
