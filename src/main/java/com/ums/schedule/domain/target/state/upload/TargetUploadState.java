package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.TargetUploadEvent;

public interface TargetUploadState {
    TargetUploadState onEvent(TargetUploadEvent event);
    TargetUploadStatusEnum currentStatus();
    default TargetUploadState onFail() {
        return new TargetUploadFailState();
    }
}
