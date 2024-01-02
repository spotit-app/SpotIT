package com.spotit.backend.abstraction;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

public abstract class IntegrationTest {

    private static final DockerImageName REDIS_IMAGE = RedisContainer.DEFAULT_IMAGE_NAME.withTag("7.0.12-alpine");

    static RedisContainer container = new RedisContainer(REDIS_IMAGE).withReuse(true);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", container::getHost);
        registry.add("spring.data.redis.port", () -> container.getMappedPort(6379).toString());
    }

    @BeforeAll
    static void beforeAll() {
        container.start();
    }
}
