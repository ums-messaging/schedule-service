package com.ums.schedule.domain.target.event;

import com.ums.schedule.application.target.upload.TargetUploadCreateCommand;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.state.upload.TargetUploadRequestState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;

public record TargetUploadRequestedEvent(
        Long requestId,
        Long uploadId,
        String templateKey,
        ChannelTypeEnum channelType,
        String objectKey) implements TargetUploadEvent, SendEvent {

    public static TargetUploadRequestedEvent of(TargetUpload targetUpload) {
        SendRequest request = targetUpload.getSendRequest();

        TargetUploadRequestedEvent event = TargetUploadRequestedEvent.of(request, targetUpload);

        return event;
    }
    private static TargetUploadRequestedEvent of(SendRequest request, TargetUpload targetUpload) {
        return new TargetUploadRequestedEvent(
                    request.getId(),
                    targetUpload.getUploadId(),
                    request.getTemplateKey(),
                    request.getChannelType(),
                    targetUpload.getObjectKey()
                );
    }
    public static TargetUploadRequestedEvent of(TargetUploadTypeEnum uploadType, Long uploadId, String objectKey, TargetUploadCreateCommand command){
        if(uploadType == TargetUploadTypeEnum.FILE && uploadId == null) {
            throw new RuntimeException();
        }
        return new TargetUploadRequestedEvent(
                command.requestId(),
                uploadId,
                command.templateKey(),
                command.channelType(),
                objectKey
        );
    }
    public static TargetUploadRequestedEvent of(Long uploadId, String objectKey, SendRequest request) {
        return new TargetUploadRequestedEvent(
                request.getId(),
                uploadId,
                request.getTemplateKey(),
                request.getChannelType(),
                objectKey
        );
    }

    @Override
    public SendRequestEventTypeEnum getEventType() {
        return SendRequestEventTypeEnum.TARGET_UPLOAD_REQUEST;
    }

    @Override
    public SendRequestStatusEnum getRequestStatus() {
        return SendRequestStatusEnum.HOLDING;
    }

    @Override
    public TargetUploadState getToStatus() {
        return new TargetUploadRequestState();
    }
}
