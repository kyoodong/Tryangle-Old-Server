package com.gomson.tryangle.api.admin.image;

import com.gomson.tryangle.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/admin/image")
public class AdminImageApiController {

    @Autowired
    private AdminImageService adminImageService;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("insert")
    private boolean insertImageList(@RequestParam("imageZip") MultipartFile imageZip)
        throws IOException {
        return adminImageService.insertImageList(
                resourceLoader.getResource("classpath:images").getFile().getAbsolutePath() + "/",
                resourceLoader.getResource("classpath:masks").getFile().getAbsolutePath() + "/",
                imageZip);
    }

    @GetMapping("{userId}")
    private List<Image> selectUnscoredImageList(@PathVariable String userId) {
        return adminImageService.selectUnscoredImageList(userId);
    }

    @PostMapping("score")
    private Boolean scoreImage(@RequestBody Map<String, Integer> dto) {
        return adminImageService.scoreImage(dto.get("imageId"), dto.get("score"));
    }

    @GetMapping("refresh")
    private Boolean refresh() throws IOException {
        return adminImageService.refresh(
                resourceLoader.getResource("classpath:images").getFile().getAbsolutePath() + "/",
                resourceLoader.getResource("classpath:masks").getFile().getAbsolutePath() + "/");
    }

    @GetMapping("refresh-cluster")
    private Boolean refreshCluster() throws IOException {
        return adminImageService.refreshCluster(resourceLoader.getResource("classpath:images").getFile().getAbsolutePath() + "/");
    }

    @GetMapping("score")
    private Integer getNumScoredImage() {
        return adminImageService.getNumScoredImage();
    }
}
