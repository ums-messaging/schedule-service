package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadFailStateException;

public class TargetUploadFailState implements TargetUploadState {
    @Override
    public TargetUploadState onEvent(TargetUploadEvent event) {
        throw TargetUploadFailStateException.of(event.getToStatus().currentStatus());
    }

    @Override
    public TargetUploadState onFail() {
        throw TargetUploadFailStateException.of(TargetUploadStatusEnum.FAIL);
    }

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.FAIL;
    }
}
