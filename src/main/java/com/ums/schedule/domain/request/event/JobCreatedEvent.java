package com.ums.schedule.domain.request.event;


import com.ums.schedule.application.channel.SendJob;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;

public record JobCreatedEvent(

) implements SendEvent {

    public static JobCreatedEvent of(SendJob job) {
        return new JobCreatedEvent();
    }

    @Override
    public SendRequestEventTypeEnum getEventType() {
        return SendRequestEventTypeEnum.JOB_CREATED;
    }

    @Override
    public SendRequestStatusEnum getRequestStatus() {
        return null;
    }
}
