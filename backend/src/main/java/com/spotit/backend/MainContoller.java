package com.spotit.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainContoller {

    @GetMapping("/")
    public String welcomeMessage() {
        return "SpotIT API v1.0";
    }
}
