package com.gomson.tryangle.api.image;

import com.gomson.tryangle.domain.component.ObjectComponent;
import com.gomson.tryangle.dto.GuideImageListDTO;
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

    /**
     * 이미지 세그멘테이션
     * @param image 사용자 사진
     * @return 가이드 이미지 url list
     */
    @PostMapping("segmentation")
    private List<ObjectComponent> imageSegmentation(@RequestParam("image") MultipartFile image)
        throws Exception{
        return imageService.imageSegmentation(image.getBytes());
    }

    /**
     * 특정 사진에 대해 가이드 이미지를 제공하는 api
     * @param image 사용자 사진
     * @return 가이드 이미지 url list
     */
    @PostMapping("recommend")
    private GuideImageListDTO recommendImage(@RequestParam("image") MultipartFile image) {
        return imageService.recommendImage(image);
    }

    /**
     * url 에 어떤 컴포넌트가 있는지 알고 싶을 때 사용하는 api
     * @param url
     * @return
     */
    @GetMapping("component")
    private List<ObjectComponent> getComponentListByUrl(@RequestParam("url") String url) {
        return imageService.getComponentListByUrl(url);
    }
}
