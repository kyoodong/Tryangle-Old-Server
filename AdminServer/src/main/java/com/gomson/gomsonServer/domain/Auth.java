package com.gomson.gomsonServer.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter @ToString
public class Auth implements Serializable {

    @Id
    private String userId;

    @Id
    private String name;
}
