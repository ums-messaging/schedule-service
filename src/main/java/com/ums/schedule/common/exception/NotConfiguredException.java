package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.CommonErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;

public abstract class NotConfiguredException extends ExternalSystemException {
    protected NotConfiguredException(String confValue) {
        super(CommonErrorCode.CONFIGURATION_LOAD_FAIL, confValue);
    }
    protected NotConfiguredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
