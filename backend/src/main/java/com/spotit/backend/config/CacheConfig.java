package com.spotit.backend.config;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory connectionrFactory) {
        return RedisCacheManager.builder(connectionrFactory)
                .cacheDefaults(RedisCacheConfiguration
                        .defaultCacheConfig()
                        .disableCachingNullValues()
                        .entryTtl(Duration.ofMinutes(5)))
                .transactionAware()
                .build();
    }

    @Bean
    CachingConfigurer customCachingConfigurer(RedisCacheManager redisCacheManager) {
        return new CustomCachingConfigurer(redisCacheManager);
    }

    class CustomCachingConfigurer implements CachingConfigurer {

        RedisCacheManager redisCacheManager;

        public CustomCachingConfigurer(RedisCacheManager redisCacheManager) {
            this.redisCacheManager = redisCacheManager;
        }

        @Override
        public CacheManager cacheManager() {
            return redisCacheManager;
        }

        @Override
        public CacheResolver cacheResolver() {
            return new RuntimeCacheResolver(cacheManager());
        }

        class RuntimeCacheResolver extends SimpleCacheResolver {

            protected RuntimeCacheResolver(CacheManager cacheManager) {
                super(cacheManager);
            }

            @Override
            protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
                String className = context.getTarget().getClass().getSimpleName();
                String entityName = className.split("Service")[0];

                return List.of(entityName);
            }
        }
    }
}
