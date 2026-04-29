package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.TargetUploadCompletedEvent;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.target.event.TargetUploadUploadedEvent;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadUploadStateException;

public class TargetUploadUploadedState implements TargetUploadState {

    @Override
    public TargetUploadState onEvent(TargetUploadEvent event) {
        if(event instanceof TargetUploadCompletedEvent) {
            return new TargetUploadCompleteState();
        } else if(event instanceof TargetUploadUploadedEvent) {
            return this;
        }
        throw TargetUploadUploadStateException.of(event.getToStatus().currentStatus());
    }

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.UPLOAD;
    }
}
