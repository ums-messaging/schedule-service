package com.ums.schedule.email.application.event;

import com.ums.schedule.email.domain.event.DomainGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetGroupEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendGroupEventListener {
    private final SendTargetGroupEventRepository repository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(SendTargetRequestEvent event) {
        List<SendTargetGroupEvent> events = event.results().stream()
                .map(v -> {
                    DomainGroupEvent groupEvent = DomainGroupEvent.of(event.requestId(), event.eventType(), v.domainGroup());
                    return SendTargetGroupEvent.of(groupEvent, event.eventResult(), v);
                }).toList();
        repository.saveAll(events);
    }
}
