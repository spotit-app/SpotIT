package com.spotit.backend.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spotit.backend.BackendApplication;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.utils.Utils;

@Configuration
public class ImageKitConfig {

    @Bean
    ImageKit imageKit() throws IOException {
        ImageKit imageKit = ImageKit.getInstance();
        io.imagekit.sdk.config.Configuration config = Utils.getSystemConfig(BackendApplication.class);
        imageKit.setConfig(config);

        return ImageKit.getInstance();
    }
}
