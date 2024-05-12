package com.spotit.backend;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {

    @GetMapping("/api")
    public Map<String, String> welcomeMessage() {
        return Map.of("message", "SpotIT API v1.0");
    }
}
