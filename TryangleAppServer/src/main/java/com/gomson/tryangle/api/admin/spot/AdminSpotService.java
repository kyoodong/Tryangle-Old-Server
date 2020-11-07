package com.gomson.tryangle.api.admin.spot;

import com.gomson.tryangle.dao.SpotDao;
import com.gomson.tryangle.domain.Spot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminSpotService {

    @Autowired
    private SpotDao spotDao;

    Spot insertSpot(Spot spot) {
        spotDao.insertSpot(spot);
        return spot;
    }
}
