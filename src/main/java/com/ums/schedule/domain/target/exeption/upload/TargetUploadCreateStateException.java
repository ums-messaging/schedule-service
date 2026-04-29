package com.ums.schedule.domain.target.exeption.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;

public class TargetUploadCreateStateException extends TargetUploadStateException {
    protected TargetUploadCreateStateException(TargetUploadStatusEnum to) {
        super(TargetUploadStatusEnum.CREATED, to);
    }

    public static TargetUploadCreateStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadCreateStateException(to);
    }
}
