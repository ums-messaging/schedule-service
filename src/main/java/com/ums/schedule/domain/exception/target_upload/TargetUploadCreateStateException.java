package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;

public class TargetUploadCreateStateException extends TargetUploadStateException {
    protected TargetUploadCreateStateException(TargetUploadStatusEnum to) {
        super(TargetUploadStatusEnum.CREATED, to);
    }

    public static TargetUploadCreateStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadCreateStateException(to);
    }
}
