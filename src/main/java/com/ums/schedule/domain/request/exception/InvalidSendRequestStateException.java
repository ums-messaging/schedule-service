package com.ums.schedule.domain.request.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.converter.state.StatusStateType;
import com.ums.schedule.common.exception.StateException;

public class InvalidSendRequestStateException extends StateException {
    protected InvalidSendRequestStateException(StatusStateType from, StatusStateType to) {
        super(from, to);
    }
    protected InvalidSendRequestStateException(ErrorCode errorCode, Object... args) {
        super(errorCode);
    }

    public static InvalidSendRequestStateException of(StatusStateType from, StatusStateType to) {
        return new InvalidSendRequestStateException(from, to);
    }

    public static InvalidSendRequestStateException of(SendRequestErrorCode errorCode) {
        return new InvalidSendRequestStateException(errorCode);
    }


}
