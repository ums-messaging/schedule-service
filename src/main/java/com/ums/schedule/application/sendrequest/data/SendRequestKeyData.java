package com.ums.schedule.application.sendrequest.data;

import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.target.upload.TargetUploadReport;

import java.util.UUID;

public record SendRequestKeyData(Long requestId, UUID messageId, ChannelType channelType) {
    public static SendRequestKeyData of(Long requestId, UUID messageId, ChannelType channelType) {
        return new SendRequestKeyData(requestId, messageId, channelType);
    }

    public static SendRequestKeyData of(UUID messageId, TargetUploadReport targetUpload) {
        SendRequest sendRequest = targetUpload.getSendRequest();
        return new SendRequestKeyData(sendRequest.getId(), messageId, sendRequest.getChannelType());
    }
}
