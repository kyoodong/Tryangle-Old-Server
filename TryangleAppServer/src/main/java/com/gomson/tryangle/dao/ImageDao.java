package com.gomson.tryangle.dao;

import com.gomson.tryangle.domain.Image;
import com.gomson.tryangle.domain.LineComponent;
import com.gomson.tryangle.domain.ObjectComponent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ImageDao {

    void insertImage(Image image);
    void insertObject(@Param("imageId") long imageId, @Param("objectComponent") ObjectComponent objectComponent);
    void insertEffectiveLine(@Param("imageId") long imageId, @Param("lineComponent") LineComponent lineComponent);
    void insertDominantColor(@Param("imageId") long imageId, @Param("colorId") long colorId);
    void insertHumanPose(@Param("objectId") long objectId, @Param("poseId") int poseId);

    List<Image> selectUnscoredImageList(String userId);

    Boolean updateImageScore(int imageId, int score);

}
