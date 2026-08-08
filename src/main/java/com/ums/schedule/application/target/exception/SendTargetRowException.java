package com.ums.schedule.application.target.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.SendTargetErrorCode;
import com.ums.schedule.common.exception.BusinessException;

public class SendTargetRowException extends BusinessException {
    protected SendTargetRowException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static SendTargetRowException of(Integer rowNo, String errorMessage) {
        return new SendTargetRowException(SendTargetErrorCode.TARGET_ROW_READ_FAILS, rowNo, errorMessage);
    }
}
