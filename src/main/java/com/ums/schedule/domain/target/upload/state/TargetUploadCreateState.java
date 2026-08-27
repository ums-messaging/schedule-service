package com.ums.schedule.domain.target.upload.state;

import com.ums.schedule.common.code.target_upload.TargetUploadEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.converter.state.StatusStateEvent;
import com.ums.schedule.domain.target.upload.exception.InvalidTargetUploadStateException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TargetUploadCreateState implements TargetUploadState {


    @Override
    public TargetUploadState onEvent(StatusStateEvent event) {
        TargetUploadEvent eventCode = TargetUploadEvent.valueOf(event.code());
        switch (eventCode) {
            case TARGET_UPLOAD_READY -> {
                return new TargetUploadWaitingState();
            }
        }
        throw InvalidTargetUploadStateException.of(
                TargetUploadStatus.CREATED, event.stateType()
        );
    }

    @Override
    public TargetUploadStatus getCurrentCode() {
        return TargetUploadStatus.CREATED;
    }
}