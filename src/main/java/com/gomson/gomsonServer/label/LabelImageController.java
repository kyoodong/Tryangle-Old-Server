package com.gomson.gomsonServer.label;

import com.gomson.gomsonServer.domain.LabelImage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("label")
public class LabelController {

    @GetMapping("{category}")
    public String categoryPage(@PathVariable String category) {
        return "category";
    }

    @GetMapping("{category}/{user}")
    public String categoryPage(@PathVariable String category, @PathVariable Integer user) {
        return "label";
    }

    @GetMapping
    @ResponseBody
    public LabelImage getImage(Integer user) {

    }
}
