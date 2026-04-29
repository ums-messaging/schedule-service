package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.TargetUploadEvent;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadCompleteStateException;

public class TargetUploadCompleteState implements TargetUploadState {

    @Override
    public TargetUploadState onEvent(TargetUploadEvent event) {
        throw TargetUploadCompleteStateException.of(event.getToStatus().currentStatus());
    }

    @Override
    public TargetUploadState onFail() {
        throw  TargetUploadCompleteStateException.of(TargetUploadStatusEnum.FAIL);
    }

    @Override
    public TargetUploadStatusEnum currentStatus() {
        return TargetUploadStatusEnum.COMPLETED;
    }
}
