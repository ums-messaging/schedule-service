package com.ums.schedule.application.target.report.model;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.util.UUID;

public record TargetUploadRequestResult(
        ChannelType channelType,
        Long requestId,
        String customerId,
        UUID messageId,
        SendRequestStatus sendRequestStatus,
        TargetUploadStatus targetUploadStatus
) {

    public static TargetUploadRequestResult of(SendRequest sendRequest, SendMessage sendMessage, TargetUploadStatus status) {
        return new TargetUploadRequestResult(
                sendRequest.getChannelType(),
                sendRequest.getId(),
                sendRequest.customerId(),
                sendMessage.getId(),
                sendRequest.getState().getCurrentCode(),
                status
        );
    }
}
