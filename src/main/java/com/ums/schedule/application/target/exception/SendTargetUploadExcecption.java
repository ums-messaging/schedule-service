package com.ums.schedule.application.target.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.SendTargetErrorCode;
import com.ums.schedule.common.exception.BusinessException;

public class SendTargetUploadExcecption extends BusinessException {
    protected SendTargetUploadExcecption(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static SendTargetUploadExcecption of(String targetKey, Throwable e) {
        return new SendTargetUploadExcecption(SendTargetErrorCode.TARGET_UPLOAD_FAIL, targetKey, e.getMessage());
    }
    public static SendTargetUploadExcecption of(SendTargetErrorCode errorCode, String targetKey) {
        return new SendTargetUploadExcecption(errorCode, targetKey);
    }
}
