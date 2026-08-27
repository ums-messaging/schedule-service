package com.ums.schedule.application.send;

import com.ums.schedule.application.ums.common.request.model.SendRequestRequestedEvent;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobManager {
    private final SendRequestRepository repository;
    private final List<JobWorker> workers;

    @Async("sendRequestExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(SendRequestRequestedEvent event) {
        SendRequest sendRequest = repository.findById(event.requestId()).orElseThrow();
        ChannelType channelType = sendRequest.getChannelType();



    }
}
