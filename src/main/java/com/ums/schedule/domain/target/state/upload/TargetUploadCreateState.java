package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.target.event.TargetUploadUrlCreatedEvent;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadCreateStateException;

public class TargetUploadCreateState implements TargetUploadState {

    @Override
    public TargetUploadState onEvent(TargetUploadEvent event) {
        if(event instanceof TargetUploadUrlCreatedEvent) {
            return new TargetUploadPendingState();
        }
        throw TargetUploadCreateStateException.of(event.getToStatus().currentStatus());
    }

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.CREATED;
    }
}