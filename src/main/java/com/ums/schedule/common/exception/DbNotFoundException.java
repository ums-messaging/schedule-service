package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.CommonErrorCode;
import com.ums.schedule.common.code.api.ErrorCode;

public abstract class DbNotFoundException extends BusinessException {
    protected DbNotFoundException(Object id, ErrorCode errorCode, Object... args) {
        super(id, errorCode, args);
    }
    protected DbNotFoundException(Object id) {
        super(CommonErrorCode.DB_NOT_FOUND, id);
    }
}
