package com.gomson.gomsonServer.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class LabelImage {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "category_id")
//    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private Category category;
    private String labeledImage;
    private int score;
    private int scoredBy;
    private LocalDateTime scoredAt;

}
