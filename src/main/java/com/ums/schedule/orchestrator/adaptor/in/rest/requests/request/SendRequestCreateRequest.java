package com.ums.schedule.orchestrator.adaptor.in.rest.requests.request;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.application.requests.command.SendRequestCreateCommand;
import com.ums.schedule.orchestrator.application.requests.config.SendRequestConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendRequestCreateRequest(
        @NotNull(message = "SEND_REQUEST:SCHEDULE_REQUIRED")
        Long scheduleId,
        String channel,
        @NotBlank(message = "SEND_REQUEST:CUSTOMER_KEY_REQUIRED")
        String customerRequestKey,
        String objectKey,
        @NotBlank(message = "SEND_REQUEST:TEMPLATE_KEY_REQUIRED")
        String messageId,
        int retryCount,
        String senderKey
) {
        public SendRequestCreateCommand toCommand(EnumMapperValue channelType,
                                                  String customerId,
                                                  SendRequestConfig config) {
                return SendRequestCreateCommand.of(
                        customerId,
                        channelType,
                        this,
                        config
                );
        }
}
