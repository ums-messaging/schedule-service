package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.CommonErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapper;

public abstract class NotSupportedException extends BusinessException {
    protected NotSupportedException(EnumMapper code, String type) {
        super(CommonErrorCode.NOT_SUPPORTED_TYPE, code.code().getSimpleName(), type);
    }
}
