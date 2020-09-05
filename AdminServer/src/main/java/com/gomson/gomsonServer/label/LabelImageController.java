package com.gomson.gomsonServer.label;

import com.gomson.gomsonServer.domain.LabelImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.tags.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("label")
@RequiredArgsConstructor
public class LabelImageController {

    private final LabelImageService labelImageService;

    @GetMapping("{category}")
    public String categoryPage(@PathVariable String category) {
        return "category";
    }

    @GetMapping("create")
    public String categoryCreatePage() {
        return "label/create";
    }

    @PostMapping("create")
    public String createCategory(MultipartFile[] images, String name) {
        labelImageService.add(images, name);
        return "redirect:/";
    }

    @GetMapping("{category}/{user}")
    public String categoryPage(@PathVariable String category, @PathVariable Integer user) {
        return "label";
    }

    @GetMapping("api/{category}/{user}/last")
    @ResponseBody
    public List<LabelImage> loadLastScoredImageList(@PathVariable String category,
                                                    @PathVariable Integer user,
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime anchorDatetime,
                                                    Integer limit) {
        return labelImageService.loadLastScoredImageList(category, user, anchorDatetime, limit);
    }

    @GetMapping("api/{category}/{user}/next")
    @ResponseBody
    public List<LabelImage> loadNextScoredImageList(@PathVariable String category,
                                                    @PathVariable Integer user,
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime anchorDatetime,
                                                    Integer limit) {
        return labelImageService.loadNextScoredImageList(category, user, anchorDatetime, limit);
    }

    @PutMapping("api/score/{imageId}")
    @ResponseBody
    public Boolean scoreImage(@PathVariable String imageId, Integer score, String imageData) {
        return labelImageService.scoreImage(imageId, score, imageData);
    }
}
