package com.ums.schedule.domain.target.event;

import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.state.upload.TargetUploadCreateState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;

public record TargetUploadCreatedEvent(
        Long uploadId,
        ChannelTypeEnum channelType,
        ChannelTemplate template
) implements TargetUploadEvent {
    public static TargetUploadCreatedEvent of(TargetUpload targetUpload, ChannelTemplate template) {
        SendRequest request = targetUpload.getSendRequest();
        return new TargetUploadCreatedEvent(
                targetUpload.getUploadId(),
                request.getChannelType(),
                template
        );
    }

    @Override
    public TargetUploadState getToStatus() {
        return new TargetUploadCreateState();
    }
}
