package com.ums.schedule.domain.target.event;

import com.ums.schedule.code.send.SendRequestEventTypeEnum;
import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.request.state.SendRequestState;
import com.ums.schedule.domain.target.state.upload.TargetUploadFailState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;
import com.ums.schedule.domain.target.upload.TargetUpload;

public record TargetUploadFailedEvent(
        Long uploadId,
        SendRequestState state,
        String errorMessage
) implements TargetUploadEvent, SendEvent {

    public static TargetUploadFailedEvent of(TargetUpload targetUpload, String errorMessage) {
        SendRequest request = targetUpload.getSendRequest();
        SendRequestState state = request.getState();

        return new TargetUploadFailedEvent(targetUpload.getUploadId(),
                state,
                errorMessage);
    }
    @Override
    public TargetUploadState getToStatus() {
        return new TargetUploadFailState();
    }

    @Override
    public SendRequestEventTypeEnum getEventType() {
        return SendRequestEventTypeEnum.TARGET_UPLOAD_REQUEST;
    }

    @Override
    public SendRequestStatusEnum getRequestStatus() {
        return SendRequestStatusEnum.HOLDING;
    }
}
