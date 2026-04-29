package com.ums.schedule.domain.target.exeption.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;

public class TargetUploadUploadStateException extends TargetUploadStateException {
    protected TargetUploadUploadStateException(TargetUploadStatusEnum to) {
        super(TargetUploadStatusEnum.UPLOAD, to);
    }

    public static TargetUploadUploadStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadUploadStateException(to);
    }
}
