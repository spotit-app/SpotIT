package com.spotit.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/**/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
}
