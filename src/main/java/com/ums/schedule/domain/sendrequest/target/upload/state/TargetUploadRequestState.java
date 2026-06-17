package com.ums.schedule.domain.sendrequest.target.upload.state;

import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadEventEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadStatusEnum;
import com.ums.schedule.domain.sendrequest.target.upload.exception.InvalidTargetUploadReportStateException;

public class TargetUploadRequestState implements TargetUploadState {
    @Override
    public TargetUploadState onEvent(StatusStateEvent event) {
        TargetUploadEventEnum eventCode = TargetUploadEventEnum.valueOf(event.code());
        switch (eventCode) {
            case TARGET_UPLOAD_STARTED -> {
                return new TargetUploadParsingState();
            }
        }
        throw InvalidTargetUploadReportStateException.of(getCurrentCode(), eventCode);
    }

    @Override
    public TargetUploadStatusEnum getCurrentCode() {
        return TargetUploadStatusEnum.REQUEST;
    }
}
