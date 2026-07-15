package com.ums.schedule.domain.request.target.upload.state;

import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;

public interface TargetUploadState extends StatusState {
    @Override
    TargetUploadState onEvent(StatusStateEvent event);

    @Override
    TargetUploadStatusEnum getCurrentCode();
}
