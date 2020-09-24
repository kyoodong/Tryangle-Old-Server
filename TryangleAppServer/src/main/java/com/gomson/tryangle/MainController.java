package com.gomson.tryangle;

import com.gomson.tryangle.config.JsonPlaceholderConfig;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class MainController {

    @GetMapping
    public String main() {
        StringBuilder sb = new StringBuilder();
        sb.append("ML_SERVER_URL = ").append(JsonPlaceholderConfig.ML_SERVER_URL);
        return sb.toString();
    }
}
