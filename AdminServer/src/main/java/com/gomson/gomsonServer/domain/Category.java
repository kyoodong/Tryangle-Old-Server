package com.gomson.gomsonServer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
public class Category {

    @Id
    private String name;
}
