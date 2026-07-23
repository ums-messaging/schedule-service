package com.ums.schedule.adapter.api.request.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendRequestCreateRequest(
        @NotNull(message = "SEND_REQUEST:SCHEDULE_REQUIRED")
        Long scheduleId,
        @NotBlank(message = "SEND_REQUEST:CUSTOMER_KEY_REQUIRED")
        String customerRequestKey,
        String uploadFormat,
        @NotBlank(message = "SEND_REQUEST:TEMPLATE_KEY_REQUIRED")
        String templateKey,
        String messageType,
        Integer retryCnt
) {
    public SendRequestCreateCommand toCommand(String customerId, String senderKey, ChannelType channelType, TargetUploadType uploadType) {
        return SendRequestCreateCommand.of(customerId, channelType, uploadType, senderKey, this);
    }
}
