package com.ums.schedule.domain.target.upload.state;

import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateEvent;

public interface TargetUploadState extends StatusState {
    @Override
    TargetUploadState onEvent(StatusStateEvent event);

    @Override
    TargetUploadStatus getCurrentCode();
}
