package com.gomson.tryangle.api.spot;

import com.gomson.tryangle.domain.Spot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/spot")
public class SpotApiController {

    @Autowired
    private SpotService spotService;

    @PostMapping
    private List<Spot> getSpotByLocation(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("image") MultipartFile image) throws IOException {
        return spotService.getSpotByLocation(lat, lon, image);
    }
}
