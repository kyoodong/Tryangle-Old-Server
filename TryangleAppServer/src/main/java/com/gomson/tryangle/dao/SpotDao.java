package com.gomson.tryangle.dao;

import com.gomson.tryangle.domain.Spot;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SpotDao {

    void insertSpot(Spot spot);

    List<Spot> selectNearSpotList(double x, double y);

}
