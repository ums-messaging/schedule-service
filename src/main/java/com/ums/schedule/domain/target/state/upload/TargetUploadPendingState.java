package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadPendingStateException;

public class TargetUploadPendingState implements TargetUploadState {
    @Override
    public TargetUploadState onEvent(TargetUploadEvent event) {
        if (event instanceof TargetUploadRequestedEvent) {
            return new TargetUploadRequestState();
        }
        throw TargetUploadPendingStateException.of(event.getToStatus().currentStatus());
    }

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.PENDING;
    }
}
