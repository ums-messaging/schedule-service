package com.ums.schedule.orchestrator.application.requests.command;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.in.rest.requests.request.SendRequestCreateRequest;
import com.ums.schedule.orchestrator.application.requests.config.SendRequestConfig;

public record SendRequestCreateCommand(
        EnumMapperValue channelType,
        Long scheduleId,
        String customerId,
        String requestKey,
        String objectKey,
        String messageId,
        String senderKey,
        int requestRetryCount,
        int configuredRetryCount
) {
    public static SendRequestCreateCommand of(String customerId,
                                              EnumMapperValue channelType,
                                              SendRequestCreateRequest request,
                                              SendRequestConfig config) {
        return new SendRequestCreateCommand(
                channelType,
                request.scheduleId(),
                customerId,
                request.customerRequestKey(),
                request.objectKey(),
                request.messageId(),
                request.senderKey(),
                request.retryCount(),
                config.retryCount()

        );
    }
}
