package com.ums.schedule.domain.request.target.upload.state;

import com.ums.schedule.common.converter.StatusStateEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadEventEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.domain.exception.target_upload.InvalidTargetUploadReportStateException;

public class TargetUploadRequestState implements TargetUploadState {
    @Override
    public TargetUploadState onEvent(StatusStateEvent event) {
        TargetUploadEventEnum eventCode = TargetUploadEventEnum.valueOf(event.code());
        switch (eventCode) {
            case TARGET_UPLOAD_STARTED -> {
                return new TargetUploadParsingState();
            }
            case TARGET_UPLOAD_FAIL -> {
                return new TargetUploadFailState();
            }
        }
        throw InvalidTargetUploadReportStateException.of(getCurrentCode(), eventCode);
    }

    @Override
    public TargetUploadStatusEnum getCurrentCode() {
        return TargetUploadStatusEnum.REQUEST;
    }
}
