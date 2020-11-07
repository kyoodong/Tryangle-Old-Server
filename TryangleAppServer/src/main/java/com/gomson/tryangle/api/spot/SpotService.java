package com.gomson.tryangle.api.spot;

import com.gomson.tryangle.dao.ImageDao;
import com.gomson.tryangle.dao.SpotDao;
import com.gomson.tryangle.domain.Image;
import com.gomson.tryangle.domain.Spot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotService {

    @Autowired
    private SpotDao spotDao;

    @Autowired
    private ImageDao imageDao;


    List<Spot> getNearSpotList(double x, double y) {
        return spotDao.selectNearSpotList(x, y);
    }

    List<String> getImageUrlBySpotId(long spotId) {
        return imageDao.selectImageUrlBySpotId(spotId);
    }
}
