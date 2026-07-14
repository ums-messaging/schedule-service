package com.ums.schedule.application.sendrequest.data;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.common.code.common.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;

public record SendRequestKeyData(Long requestId, String messageId, ChannelTypeEnum channelType) {
    public static SendRequestKeyData of(Long requestId, String messageId, ChannelTypeEnum channelType) {
        return new SendRequestKeyData(requestId, messageId, channelType);
    }

    public static SendRequestKeyData of(String messageId, TargetUploadReport targetUpload) {
        SendRequest sendRequest = targetUpload.getSendRequest();
        return new SendRequestKeyData(sendRequest.getId(), messageId, sendRequest.getChannelType());
    }
}
