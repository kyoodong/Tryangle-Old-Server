package com.gomson.gomsonServer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImageController {

    @GetMapping
    public String imageMain() {
        return "image_main";
    }
}
