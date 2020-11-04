package com.gomson.tryangle.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    private long id;
    private String url;
    private String author;
    private int score;
    private int cluster;
    private LocalDateTime createdAt;
    private LocalDateTime scoredAt;

}
