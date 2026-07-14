package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;

public class TargetUploadParsingStateException extends TargetUploadStateException {
    protected TargetUploadParsingStateException(TargetUploadStatusEnum to) {
        super(TargetUploadStatusEnum.PARSING, to);
    }

    public static TargetUploadParsingStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadParsingStateException(to);
    }
}
