package com.ums.schedule.orchestrator.application.channel.event;

public interface ChannelRequestedEvent<T> {
    Long requestId();
    T getMessageId();
}
