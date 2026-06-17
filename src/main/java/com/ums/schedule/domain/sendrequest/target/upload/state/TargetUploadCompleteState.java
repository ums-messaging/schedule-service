package com.ums.schedule.domain.sendrequest.target.upload.state;

import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadEventEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadStatusEnum;
import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.sendrequest.target.upload.exception.InvalidTargetUploadReportStateException;

public class TargetUploadCompleteState implements TargetUploadState {

    @Override
    public TargetUploadState onEvent(StatusStateEvent event) {
        TargetUploadEventEnum eventCode = TargetUploadEventEnum.valueOf(event.code());
        throw InvalidTargetUploadReportStateException.of(getCurrentCode(), eventCode);
    }

    @Override
    public TargetUploadStatusEnum getCurrentCode() {
        return TargetUploadStatusEnum.COMPLETED;
    }
}
