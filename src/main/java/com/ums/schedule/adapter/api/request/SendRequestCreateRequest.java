package com.ums.schedule.adapter.api.request;

import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;

public record SendRequestCreateRequest(
        Long scheduleId,
        String customerRequestKey,
        String uploadFormat,
        String senderKey,
        String templateKey,
        String messageType,
        Integer retryCnt
) {
    public SendRequestCreateCommand toCommand(String customerId, ChannelType channelType, TargetUploadTypeEnum uploadType) {
        return SendRequestCreateCommand.of(customerId, channelType, uploadType, this);
    }
}
