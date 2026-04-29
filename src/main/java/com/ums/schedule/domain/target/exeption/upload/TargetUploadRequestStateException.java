package com.ums.schedule.domain.target.exeption.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;

public class TargetUploadRequestStateException extends TargetUploadStateException {

    protected TargetUploadRequestStateException(TargetUploadStatusEnum from) {
        super(from, TargetUploadStatusEnum.REQUEST);
    }

    public static TargetUploadRequestStateException of(TargetUploadStatusEnum from) {
        return new TargetUploadRequestStateException(from);
    }

}
