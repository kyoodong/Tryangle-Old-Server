package com.gomson.tryangle.api.mobile_model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@PropertySource("classpath:model_config.yml")
@Service
public class MobileModelService {

    @Value("${model.latest.version}")
    private String latestModelVersion;

    @Value("${feature.latest.version}")
    private String latestFeatureVersion;

    String getLatestModelVersion() {
        return latestModelVersion;
    }

    String getLatestFeatureVersion() {
        return latestFeatureVersion;
    }
}
