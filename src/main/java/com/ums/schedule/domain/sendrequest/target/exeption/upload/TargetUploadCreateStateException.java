package com.ums.schedule.domain.sendrequest.target.exeption.upload;

import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadStatusEnum;

public class TargetUploadCreateStateException extends TargetUploadStateException {
    protected TargetUploadCreateStateException(TargetUploadStatusEnum to) {
        super(TargetUploadStatusEnum.CREATED, to);
    }

    public static TargetUploadCreateStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadCreateStateException(to);
    }
}
