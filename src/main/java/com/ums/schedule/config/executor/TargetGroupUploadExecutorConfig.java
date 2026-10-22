package com.ums.schedule.config.executor;

import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class TargetGroupUploadExecutorConfig {

    @Bean(name = "targetGroupUploadExecutor")
    public ThreadPoolTaskExecutor targetGroupUploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);        // 워크로드에 맞게 조정
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("target-group-upload-");
        executor.initialize();
        return executor;
    }
}
