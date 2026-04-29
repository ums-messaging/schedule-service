package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.TargetMessageCreatedEvent;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadRequestStateException;

public class TargetUploadRequestState implements TargetUploadState {
    @Override
    public TargetUploadState onEvent(TargetUploadEvent event) {
        if(event instanceof TargetMessageCreatedEvent) {
            return new TargetUploadParsingState();
        }
        throw TargetUploadRequestStateException.of(event.getToStatus().currentStatus());
    }

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.REQUEST;
    }
}
