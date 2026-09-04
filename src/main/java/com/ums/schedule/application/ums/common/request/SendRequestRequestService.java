package com.ums.schedule.application.ums.common.request;

import com.ums.schedule.application.ums.common.request.model.SendRequestRequestedEvent;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendRequestRequestService {
    private final SendRequestRepository repository;
    private final ApplicationEventPublisher publisher;

    public Long request(Long requestId) {
        SendRequest sendRequest = repository.findById(requestId).orElseThrow();
        sendRequest.requestSend();
        SendRequestRequestedEvent event = SendRequestRequestedEvent.of(sendRequest);
        publisher.publishEvent(event);
        return sendRequest.getId();
    }
}
