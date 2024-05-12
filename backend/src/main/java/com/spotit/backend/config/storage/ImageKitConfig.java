package com.spotit.backend.config.storage;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.imagekit.sdk.ImageKit;

@Configuration
public class ImageKitConfig {

    @Value("${IMAGEKIT_URL:https://example.com}")
    private String imagekitUrl;

    @Value("${IMAGEKIT_PUBLIC_KEY:example-key}")
    private String imagekitPublicKey;

    @Value("${IMAGEKIT_PRIVATE_KEY:example-key}")
    private String imagekitPrivateKey;

    @Bean
    ImageKit imageKit() throws IOException {
        ImageKit imageKit = ImageKit.getInstance();

        io.imagekit.sdk.config.Configuration config = new io.imagekit.sdk.config.Configuration();

        config.setUrlEndpoint(imagekitUrl);
        config.setPublicKey(imagekitPublicKey);
        config.setPrivateKey(imagekitPrivateKey);

        imageKit.setConfig(config);

        return ImageKit.getInstance();
    }
}
