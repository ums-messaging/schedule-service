package com.ums.schedule.domain.request.event;

import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;

public record SendRequestedEvent(
        Long requestId,
        ChannelTypeEnum channelType,
        ScheduleTypeEnum scheduleType
) implements SendEvent {

    public static SendRequestedEvent of(SendRequest request) {
        return new SendRequestedEvent(
                request.getId(),
                request.getChannelType(),
                request.getSchedule().getCyclePolicy().getScheduleType()
        );
    }

    @Override
    public SendRequestEventTypeEnum getEventType() {
        return SendRequestEventTypeEnum.SEND_REQUESTED;
    }

    @Override
    public SendRequestStatusEnum getRequestStatus() {
        return SendRequestStatusEnum.REQUEST;
    }
}
