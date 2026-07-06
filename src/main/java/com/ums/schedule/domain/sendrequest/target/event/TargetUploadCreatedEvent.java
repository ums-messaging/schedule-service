package com.ums.schedule.domain.sendrequest.target.event;

import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.template.ChannelTemplate;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadCreateState;
import com.ums.schedule.domain.sendrequest.target.upload.state.TargetUploadState;

public record TargetUploadCreatedEvent(
        Long uploadId,
        ChannelTypeEnum channelType,
        ChannelTemplate template
) implements TargetUploadEvent {
    public static TargetUploadCreatedEvent of(TargetUploadReport targetUpload, ChannelTemplate template) {
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
