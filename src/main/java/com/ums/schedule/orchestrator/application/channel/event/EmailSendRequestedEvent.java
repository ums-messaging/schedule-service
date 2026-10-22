package com.ums.schedule.orchestrator.application.channel.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ums.schedule.orchestrator.application.requests.SendRequestedEvent;

public record EmailSendRequestedEvent<T>(
        @JsonUnwrapped
        SendRequestedEvent event,
        @JsonUnwrapped
        T messageId
) implements ChannelRequestedEvent {

    @Override
    public Long requestId() {
        return event.requestId();
    }

    @Override
    public T getMessageId() {
        return this.messageId;
    }
}
