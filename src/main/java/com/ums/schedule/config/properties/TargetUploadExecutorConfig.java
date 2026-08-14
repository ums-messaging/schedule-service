package com.ums.schedule.config.properties;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class TargetUploadExecutorConfig {
    private final MeterRegistry registry;
    private  ThreadPoolExecutor executor;

    @Bean(name = "targetUploadExecutor")
    public ThreadPoolTaskExecutor targetUploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("target-upload-");
        executor.initialize();

        this.executor = executor.getThreadPoolExecutor();

        ExecutorServiceMetrics.monitor(
                registry,
                this.executor,
                "targetUploadExecutor"
        );
        return executor;
    }
}
