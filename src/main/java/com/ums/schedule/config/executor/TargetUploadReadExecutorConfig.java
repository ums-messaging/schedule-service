package com.ums.schedule.config.executor;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class TargetUploadReadExecutorConfig {

    @Bean(name = "targetUploadReadExecutor")
    public ThreadPoolTaskExecutor targetUploadReadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);        // 워크로드에 맞게 조정
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("target-upload-read-");
        executor.initialize();

        return executor;
    }

}
