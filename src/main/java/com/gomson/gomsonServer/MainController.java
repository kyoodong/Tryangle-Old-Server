package com.gomson.gomsonServer;

import com.gomson.gomsonServer.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;

    @GetMapping
    public ModelAndView main() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("categories", categoryService.findAll());
        mv.setViewName("index");
        return mv;
    }
}
