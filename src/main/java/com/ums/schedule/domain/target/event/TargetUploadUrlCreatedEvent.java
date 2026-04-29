package com.ums.schedule.domain.target.event;

import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.state.upload.TargetUploadPendingState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;

public record TargetUploadUrlCreatedEvent(

) implements TargetUploadEvent {

    public static TargetUploadUrlCreatedEvent of(TargetUpload targetUpload) {
        return new TargetUploadUrlCreatedEvent();
    }

    @Override
    public TargetUploadState getToStatus() {
        return new TargetUploadPendingState();
    }
}
