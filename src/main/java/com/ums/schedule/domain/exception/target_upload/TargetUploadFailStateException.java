package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;

public class TargetUploadFailStateException extends TargetUploadStateException {
    protected TargetUploadFailStateException(TargetUploadStatusEnum from, TargetUploadStatusEnum to) {
        super(from, to);
    }

    public static TargetUploadFailStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadFailStateException(TargetUploadStatusEnum.FAIL, to);
    }
}
