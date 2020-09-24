package com.gomson.tryangle.api.image;

import com.gomson.tryangle.domain.ObjectComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/image")
public class ImageApiController {

    @Autowired
    private ImageService imageService;


    @GetMapping
    private String g() {
        return "hihi";
    }

    @PostMapping
    private List<String> recommendImage(@RequestParam("image") MultipartFile image) {
        return imageService.recommendImage(image);
    }

    @GetMapping("component")
    private List<ObjectComponent> getComponentListByUrl(@RequestParam("url") String url) {
        return imageService.getComponentListByUrl(url);
    }
}
