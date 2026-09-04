package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.CommonErrorCode;

public abstract class RequiredValueMissingException extends BusinessException {
    protected RequiredValueMissingException(String code, String fieldName) {
        super(CommonErrorCode.REQUIRED_VALUE, code, fieldName);
    }
}
