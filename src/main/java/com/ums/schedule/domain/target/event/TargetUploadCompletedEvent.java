package com.ums.schedule.domain.target.event;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.state.upload.TargetUploadCompleteState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;

public record TargetUploadCompletedEvent(
    Long uploadId,
    Integer uploadTotalSize
) implements TargetUploadEvent, SendEvent {

    public static TargetUploadCompletedEvent of(TargetUpload targetUpload) {
        return new TargetUploadCompletedEvent(
                targetUpload.getUploadId(),
                targetUpload.getTargetList().size()
        );
    }

    @Override
    public SendRequestEventTypeEnum getEventType() {
        return SendRequestEventTypeEnum.TARGET_UPLOAD_COMPLETED;
    }

    @Override
    public SendRequestStatusEnum getRequestStatus() {
        return SendRequestStatusEnum.READY;
    }

    @Override
    public TargetUploadState getToStatus() {
        return new TargetUploadCompleteState();
    }
}