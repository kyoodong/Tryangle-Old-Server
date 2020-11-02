package com.gomson.tryangle.domain.component;

import com.gomson.tryangle.domain.guide.Guide;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class Component {

    private long id;
    private long componentId;
    private ArrayList<? extends Guide> guideList;

}
