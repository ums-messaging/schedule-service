package com.ums.schedule.application.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.application.channel.JobCreator;
import com.ums.schedule.application.channel.SendJob;
import com.ums.schedule.domain.request.event.JobCreatedEvent;
import com.ums.schedule.domain.request.event.SendRequestedEvent;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SendRequestedEventListener {
    private final ObjectMapper mapper;
    private final JobCreator jobCreator;
    private final ApplicationEventPublisher publisher;
    private final SendRequestRepository requestRepository;

    @Async
    @EventListener
    public void listen(SendRequestedEvent event) {
        Long requestId = event.requestId();
        SendRequest request = requestRepository.findById(requestId).orElseThrow();
        SendJob job = jobCreator.createJob(request);
        JobCreatedEvent jobCreatedEvent = JobCreatedEvent.of(job);
        publisher.publishEvent(jobCreatedEvent);
    }
}
