package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.target.event.SendTargetFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
@RequiredArgsConstructor
public class FailureSendTargetWriterListener {
    private final AwsS3Repository fileRepository;

    @Async
    @EventListener
    public void listen(SendTargetFailedEvent event) {

    }
}

