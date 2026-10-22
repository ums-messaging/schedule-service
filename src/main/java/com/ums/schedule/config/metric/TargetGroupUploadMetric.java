package com.ums.schedule.config.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class TargetGroupUploadMetric {
    private final AtomicInteger activeTasks = new AtomicInteger();

    public TargetGroupUploadMetric(MeterRegistry registry) {
        Gauge.builder(
                        "target_group_upload_active_tasks",
                        activeTasks,
                        AtomicInteger::get
                )
                .description("Currently running target upload tasks")
                .register(registry);
    }

    public void start() {
        activeTasks.incrementAndGet();
    }

    public void complete() {
        activeTasks.decrementAndGet();
    }
}
