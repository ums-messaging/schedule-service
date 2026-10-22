package com.ums.schedule.orchestrator.adaptor.out.queue;

import com.ums.schedule.orchestrator.application.requests.result.SendRequestRequestContext;

public interface SendRequestEventDispatcher {
    boolean supports(String mode);
    void publish(String topic,  SendRequestRequestContext context);
}
