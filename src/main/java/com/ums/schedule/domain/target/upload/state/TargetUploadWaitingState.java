package com.ums.schedule.domain.target.upload.state;

import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.domain.target.upload.exception.InvalidTargetUploadStateException;

public class TargetUploadWaitingState implements TargetUploadState {


    @Override
    public TargetUploadState onEvent(StatusStateEvent event) {
        TargetUploadEvent eventCode = TargetUploadEvent.valueOf(event.code());
        switch (eventCode) {
            case TARGET_UPLOAD_REQUESTED -> {
                return new TargetUploadRequestState();
            }
        }
        throw InvalidTargetUploadStateException.of(TargetUploadStatus.WAITING, eventCode.stateType());
    }

    @Override
    public TargetUploadStatus getCurrentCode() {
        return TargetUploadStatus.WAITING;
    }
}