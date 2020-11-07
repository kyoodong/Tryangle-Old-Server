package com.gomson.tryangle.api.spot;

import com.gomson.tryangle.domain.Spot;
import com.gomson.tryangle.dto.GuideImageListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/spot")
public class SpotApiController {

    @Autowired
    private SpotService spotService;

    @GetMapping
    private List<Spot> getNearSpotList(@RequestParam double x, @RequestParam double y) {
        return spotService.getNearSpotList(x, y);
    }

    @GetMapping
    private List<String> getImageUrlBySpotId(@RequestParam long spotId) {
        return spotService.getImageUrlBySpotId(spotId);
    }

}
