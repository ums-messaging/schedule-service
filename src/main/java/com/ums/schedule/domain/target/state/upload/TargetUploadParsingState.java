package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.target.event.TargetUploadUploadedEvent;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadParsingStateException;

public class TargetUploadParsingState implements TargetUploadState {

    @Override
    public TargetUploadState onEvent(TargetUploadEvent event) {
       if(event instanceof TargetUploadUploadedEvent) {
           return new TargetUploadUploadedState();
       }
       throw TargetUploadParsingStateException.of(event.getToStatus().currentStatus());
    }

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.PARSING;
    }
}
