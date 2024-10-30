package com.ceos20.instagram.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {
    @Bean(name="ImageUploadExecutor")
    public Executor imageUploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(9); //하나의 게시글에 올릴 수 있는 이미지 개수
        executor.setMaxPoolSize(18); //두명이 동시작업 할 수 있도록
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("ImageUpload-");
        executor.initialize();
        return executor;
    }
}
