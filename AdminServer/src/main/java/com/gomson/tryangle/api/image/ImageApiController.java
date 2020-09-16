package com.gomson.tryangle.api.image;

import com.gomson.tryangle.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/image")
public class ImageApiController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("insert")
    private boolean insertImageList(HttpServletRequest request, @RequestParam("images") List<MultipartFile> imageList)
        throws IOException {
        imageService.insertImageList(
                resourceLoader.getResource("classpath:images").getFile().getAbsolutePath() + "/",
                imageList);
        return true;
    }

    @GetMapping("{userId}")
    private List<Image> selectUnscoredImageList(@PathVariable String userId) {
        return imageService.selectUnscoredImageList(userId);
    }



    @PostMapping("score")
    private Boolean scoreImage(@RequestBody Map<String, Integer> dto) {
        return imageService.scoreImage(dto.get("imageId"), dto.get("score"));
    }
}
