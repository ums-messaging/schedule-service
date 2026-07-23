package com.ums.schedule.domain.target.upload.state;

import com.ums.schedule.common.code.target_upload.TargetUploadEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.target.upload.exception.InvalidTargetUploadStateException;

public class TargetUploadCompleteState implements TargetUploadState {

    @Override
    public TargetUploadState onEvent(StatusStateEvent event) {
        TargetUploadEvent eventCode = TargetUploadEvent.valueOf(event.code());
        throw InvalidTargetUploadStateException.of(TargetUploadStatus.COMPLETED, eventCode.stateType());
    }

    @Override
    public TargetUploadStatus getCurrentCode() {
        return TargetUploadStatus.COMPLETED;
    }
}
