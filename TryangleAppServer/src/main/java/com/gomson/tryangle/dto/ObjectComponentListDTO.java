package com.gomson.tryangle.dto;

import com.gomson.tryangle.domain.component.ObjectComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ObjectComponentListDTO {

    private List<ObjectComponent> objectComponentList;
    private String maskStr;

}
