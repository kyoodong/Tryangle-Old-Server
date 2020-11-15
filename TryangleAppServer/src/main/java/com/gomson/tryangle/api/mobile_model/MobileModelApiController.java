package com.gomson.tryangle.api.mobile_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/mobile-model")
public class MobileModelApiController {

    @Autowired
    private MobileModelService mobileModelService;

    @GetMapping("version/model")
    private String getLatestModelVersion() {
        return mobileModelService.getLatestModelVersion();
    }

    @GetMapping("version/feature")
    private String getLatestFeatureVersion() {
        return mobileModelService.getLatestFeatureVersion();
    }
}
