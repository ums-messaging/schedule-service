package com.ums.schedule.orchestrator.domain.upload.state;

import com.ums.schedule.orchestrator.common.code.SendRequestUploadStatus;
import com.ums.schedule.common.converter.state.StatusState;
import com.ums.schedule.common.converter.state.StatusStateEvent;

public interface SendRequestUploadState extends StatusState {
    @Override
    SendRequestUploadState onEvent(StatusStateEvent event);

    @Override
    SendRequestUploadStatus getCurrentCode();
}
