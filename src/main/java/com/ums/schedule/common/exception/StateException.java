package com.ums.schedule.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.StateErrorCode;
import com.ums.schedule.common.converter.StatusStateType;

public abstract class StateException extends BusinessException {
    protected StateException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
    protected StateException(StatusStateType from, StatusStateType to) {
        super(StateErrorCode.CANNOT_CHANGE_TO_STATE, from.description(), to.description());
    }
}
