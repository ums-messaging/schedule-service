package com.ums.schedule.email.application.event;

import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetGroupEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendGroupRequestEventService {
    private final SendTargetGroupEventRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(SendTargetGroupEvent event) {
        try {
            log.info("event = {}", event);
            repository.save(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
