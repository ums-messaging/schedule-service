package com.ums.schedule.domain.target.upload.state;

import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.domain.target.upload.exception.InvalidTargetUploadStateException;

public class TargetUploadRequestState implements TargetUploadState {
    @Override
    public TargetUploadState onEvent(StatusStateEvent event) {
        TargetUploadEvent eventCode = TargetUploadEvent.valueOf(event.code());
        switch (eventCode) {
            case TARGET_UPLOAD_STARTED -> {
                return new TargetUploadParsingState();
            }
            case TARGET_UPLOAD_FAIL -> {
                return new TargetUploadFailState();
            }
        }
        throw InvalidTargetUploadStateException.of(TargetUploadStatus.REQUEST, event.stateType());
    }

    @Override
    public TargetUploadStatus getCurrentCode() {
        return TargetUploadStatus.REQUEST;
    }
}
