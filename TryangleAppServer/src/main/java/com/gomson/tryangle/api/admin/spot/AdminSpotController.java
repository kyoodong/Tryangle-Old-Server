package com.gomson.tryangle.api.admin.spot;

import com.gomson.tryangle.domain.Spot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/spot")
public class AdminSpotController {

    @Autowired
    private AdminSpotService service;

    @PostMapping("")
    private Spot addSpot(@RequestBody Spot spot) {
        return service.insertSpot(spot);
    }
}
