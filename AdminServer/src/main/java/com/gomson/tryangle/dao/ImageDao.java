package com.gomson.tryangle.dao;

import com.gomson.tryangle.domain.Image;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ImageDao {

    void insertImage(Image image);

    List<Image> selectUnscoredImageList(String userId);

    Boolean updateImageScore(int imageId, int score);

}
