package com.ums.schedule.application.ums.common.request.model;

import com.ums.schedule.application.target.report.model.TargetUploadResult;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;

import java.util.UUID;

public record SendRequestCreateResult(
        Long requestId,
        String messageId,
        ChannelType channelType,
        Integer retryCount,
        TargetUploadResult targetUploadReport
) {

    public static SendRequestCreateResult of(UUID messageId, SendRequest sendRequest, TargetUploadResult result) {
        SendMessage message = sendRequest.getSendMessage();
        return new SendRequestCreateResult(
                sendRequest.getId(),
                messageId.toString(),
                sendRequest.getChannelType(),
                sendRequest.getRetryCnt(),
                result
        );
    }
}
