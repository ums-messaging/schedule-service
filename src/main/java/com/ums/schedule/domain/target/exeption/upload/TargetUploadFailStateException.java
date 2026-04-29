package com.ums.schedule.domain.target.exeption.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.state.upload.TargetUploadFailState;

public class TargetUploadFailStateException extends TargetUploadStateException {
    protected TargetUploadFailStateException(TargetUploadStatusEnum from, TargetUploadStatusEnum to) {
        super(from, to);
    }

    public static TargetUploadFailStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadFailStateException(TargetUploadStatusEnum.FAIL, to);
    }
}
