package com.gomson.gomsonServer;

import com.gomson.gomsonServer.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ImageController {

    @GetMapping("image")
    public ModelAndView imageMain(@RequestParam(required = false) String code, @AuthenticationPrincipal User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("code", code);
        modelAndView.setViewName("image_main");
        return modelAndView;
    }
}
