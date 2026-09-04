package com.ums.schedule.config.properties;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class TargetUploadFileReaderExecutorConfig {
    private final MeterRegistry registry;

    @Bean(name = "targetUploadFileExecutor")
    public ThreadPoolTaskExecutor targetUploadFileReaderExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);        // 워크로드에 맞게 조정
        executor.setMaxPoolSize(15);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("target-upload-reader-");
        executor.initialize();

        ExecutorServiceMetrics.monitor(
                registry,
                executor.getThreadPoolExecutor(),
                "targetUploadFileReaderExecutor"
        );

        return executor;
    }

}
