package com.gomson.tryangle.dao;

import com.gomson.tryangle.domain.Image;
import com.gomson.tryangle.domain.Point;
import com.gomson.tryangle.domain.component.LineComponent;
import com.gomson.tryangle.domain.component.ObjectComponent;
import com.gomson.tryangle.domain.component.PersonComponent;
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
    void insertHumanPose(
            @Param("objectId") long objectId,
            @Param("poseId") int poseId,
            @Param("posePoints") Map<String, Point> posePoints);

    List<Image> selectUnscoredImageList(String userId);

    Boolean updateImageScore(@Param("imageId") int imageId, @Param("score") int score);

    // 객체 수 = 무조건 맞아야 함, 크기 = 어느 정도, 위치 = 어느 정도
    List<String> selectImageUrlByObjects(
            List<ObjectComponent> objectComponentList,
            int areaThreshold,
            int positionThreshold);

    List<Image> selectImageByObject(int objectId);
    List<Image> selectSinglePersonImage();

    List<String> selectImageUrlByPerson(List<PersonComponent> personComponentList,
                                        int areaThreshold,
                                        int positionThreshold);

    List<String> selectImageUrlByCluster(int cluster, List<Integer> colorList);

    List<ObjectComponent> selectComponentByUrl(String url);
    String selectUrlById(long imageId);

    List<Image> selectUnmaskedImageList();
    List<Image> selectAllImageList();
    int deleteImage(long id);

    Integer getNumScoredImage();

    void updateCluster(@Param("imageId") long imageId,
                       @Param("cluster") int cluster);

    void deleteImageObject(long imageId);
    void deleteImageDominantColor(long imageId);
}
