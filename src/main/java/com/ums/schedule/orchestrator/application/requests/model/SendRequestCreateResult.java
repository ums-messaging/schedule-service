package com.ums.schedule.orchestrator.application.requests.model;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;

public record SendRequestCreateResult(
        Long requestId,
        String customerId,
        String customerKey,
        String messageId,
        ChannelType channelType,
        ScheduleType scheduleType,
        CycleCd cycleCd,
        String cycleValue,
        Integer retryCount
) {
    public static SendRequestCreateResult of(String messageId, Schedule schedule, SendRequest sendRequest) {
        return new SendRequestCreateResult(
                sendRequest.getId(),
                sendRequest.customerId(),
                sendRequest.customerRequestId(),
                messageId,
                sendRequest.getChannelType(),
                schedule.getCyclePolicy().getScheduleType(),
                schedule.getCyclePolicy().getCycleCd(),
                schedule.getCyclePolicy().getCycleValue(),
                sendRequest.getRetryCount()
        );
    }
}
