package com.ums.schedule.domain.target.exeption.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;

public class TargetUploadPendingStateException extends TargetUploadStateException {
    protected TargetUploadPendingStateException(TargetUploadStatusEnum to) {
        super(TargetUploadStatusEnum.PENDING, to);
    }

    public static TargetUploadPendingStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadPendingStateException(to);
    }
}
