package com.ums.schedule.domain.target.event;

import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;
import com.ums.schedule.domain.target.state.upload.TargetUploadUploadedState;

public record TargetUploadUploadedEvent() implements TargetUploadEvent {

    public static TargetUploadUploadedEvent of(TargetUpload targetUpload) {
        return new TargetUploadUploadedEvent();
    }


    @Override
    public TargetUploadState getToStatus() {
        return new TargetUploadUploadedState();
    }
}
