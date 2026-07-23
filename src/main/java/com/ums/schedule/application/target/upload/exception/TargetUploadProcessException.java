package com.ums.schedule.application.target.upload.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.exception.BusinessException;

public class TargetUploadProcessException extends BusinessException {
    protected TargetUploadProcessException(TargetUploadErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static TargetUploadProcessException of(TargetUploadErrorCode errorCode, Throwable e) {
        return new TargetUploadProcessException(errorCode, e.getMessage());
    }
}
