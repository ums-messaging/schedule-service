package com.ums.schedule.domain.sendrequest.target.exeption.upload;

import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadStatusEnum;

public class TargetUploadParsingStateException extends TargetUploadStateException {
    protected TargetUploadParsingStateException(TargetUploadStatusEnum to) {
        super(TargetUploadStatusEnum.PARSING, to);
    }

    public static TargetUploadParsingStateException of(TargetUploadStatusEnum to) {
        return new TargetUploadParsingStateException(to);
    }
}
