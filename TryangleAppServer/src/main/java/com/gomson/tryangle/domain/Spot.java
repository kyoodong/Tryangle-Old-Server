package com.gomson.tryangle.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Spot {

    private long id;
    private String name;
    private float x;
    private float y;
}
