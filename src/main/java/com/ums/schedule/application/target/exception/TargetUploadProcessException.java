package com.ums.schedule.application.target.exception;

import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.exception.BusinessException;

public class TargetUploadProcessException extends BusinessException {
    protected TargetUploadProcessException(TargetUploadErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static TargetUploadProcessException of(TargetUploadErrorCode errorCode, Throwable e) {
        return new TargetUploadProcessException(errorCode, e.getMessage());
    }

    public static TargetUploadProcessException of(TargetUploadErrorCode errorCode, String... args) {
        return new TargetUploadProcessException(errorCode, args);
    }
}
