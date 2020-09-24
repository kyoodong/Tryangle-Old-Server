package com.gomson.tryangle.dao;

import com.gomson.tryangle.domain.Image;
import com.gomson.tryangle.domain.LineComponent;
import com.gomson.tryangle.domain.ObjectComponent;
import com.gomson.tryangle.domain.PersonComponent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ImageDao {

    void insertImage(Image image);
    void insertObject(@Param("imageId") long imageId, @Param("objectComponent") ObjectComponent objectComponent);
    void insertEffectiveLine(@Param("imageId") long imageId, @Param("lineComponent") LineComponent lineComponent);
    void insertDominantColor(@Param("imageId") long imageId, @Param("colorId") long colorId);
    void insertHumanPose(@Param("objectId") long objectId, @Param("poseId") int poseId);

    List<Image> selectUnscoredImageList(String userId);

    Boolean updateImageScore(@Param("imageId") int imageId, @Param("score") int score);

    // 객체 수 = 무조건 맞아야 함, 크기 = 어느 정도, 위치 = 어느 정도
    List<String> selectImageUrlByObject(
            List<ObjectComponent> objectComponentList,
            int areaThreshold,
            int positionThreshold);

    List<String> selectImageUrlByPerson(List<PersonComponent> personComponentList,
                                        int areaThreshold,
                                        int positionThreshold);

    List<ObjectComponent> selectComponentByUrl(String url);

    List<Image> selectUnmaskedImageList();
    void deleteImage(long id);

    Integer getNumScoredImage();
}
