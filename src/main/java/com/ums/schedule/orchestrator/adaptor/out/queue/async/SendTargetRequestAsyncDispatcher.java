package com.ums.schedule.orchestrator.adaptor.out.queue.async;

import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.application.event.SendTargetRequestEvent;
import com.ums.schedule.email.application.target.SendTargetUploadResult;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import com.ums.schedule.orchestrator.adaptor.out.queue.SendTargetRequestDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class SendTargetRequestAsyncDispatcher implements SendTargetRequestDispatcher {
    private final ApplicationEventPublisher publisher;

    @Override
    public CompletableFuture<SendTargetResultList> publish(SendJob job, DomainGroup domainGroup, SendTargetResultList resultList) {
        return null;
    }
}
