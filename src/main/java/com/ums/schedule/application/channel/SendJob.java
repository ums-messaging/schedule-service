package com.ums.schedule.application.channel;

import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.domain.request.SendRequest;

public record SendJob(
        Long requestId,
        ChannelTypeEnum channelType,
        ScheduleTypeEnum scheduleType,
        String topicName
) {

    public static SendJob of(SendRequest request) {
        return new SendJob(
                request.getId(),
                request.getChannelType(),
                request.getSchedule().getCyclePolicy().scheduleType(),
                request.getChannelType().value()
        );
    }
}
