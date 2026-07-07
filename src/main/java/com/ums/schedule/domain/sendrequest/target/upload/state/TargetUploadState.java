package com.ums.schedule.domain.sendrequest.target.upload.state;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.state.SendRequestState;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadStatusEnum;
import com.ums.schedule.common.converter.StatusState;
import com.ums.schedule.common.converter.StatusStateEvent;

public interface TargetUploadState extends StatusState {
    @Override
    TargetUploadState onEvent(StatusStateEvent event);

    @Override
    TargetUploadStatusEnum getCurrentCode();
}
