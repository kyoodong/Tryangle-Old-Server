package com.gomson.gomsonServer.label;

import com.gomson.gomsonServer.domain.LabelImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.tags.Param;

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
        System.out.println(name + images.length);
        labelImageService.add(images, name);
        return "redirect:/";
    }

    @GetMapping("{category}/{user}")
    public String categoryPage(@PathVariable String category, @PathVariable Integer user) {
        return "label";
    }

    @GetMapping("{category}/{user}/{page}")
    @ResponseBody
    public List<LabelImage> getImage(@PathVariable String category, @PathVariable Integer user,
                                     @PathVariable Integer page) {
        return labelImageService.findList(user);
    }
}
