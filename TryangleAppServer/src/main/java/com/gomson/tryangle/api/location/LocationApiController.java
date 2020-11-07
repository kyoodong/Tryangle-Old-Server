package com.gomson.tryangle.api.location;

import com.gomson.tryangle.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/location")
public class LocationApiController {

    @Autowired
    private LocationService locationService;


    @PostMapping("image")
    private List<Image> addLocationImage() {

    }

}
