package com.ums.schedule.domain.sendrequest.target.exeption.upload;

import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadStatusEnum;

public class TargetUploadFailStateException extends TargetUploadStateException {
    protected TargetUploadFailStateException(TargetUploadStatusEnum from, TargetUploadStatusEnum to) {
        super(from, to);
    }

    public static TargetUploadFailStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadFailStateException(TargetUploadStatusEnum.FAIL, to);
    }
}
