package com.gomson.tryangle.api.spot;

import com.gomson.tryangle.domain.Spot;
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
    private List<Spot> getSpotByLocation(@RequestParam("x") double x, @RequestParam("y") double y) {
        return spotService.getSpotByLocation(x, y);
    }
}
