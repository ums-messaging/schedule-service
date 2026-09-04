package com.ums.schedule.domain.target.exception;

import com.ums.schedule.common.code.api.SendTargetErrorCode;
import com.ums.schedule.common.exception.ValidationException;

public class SendTargetMessageVariableMissingException extends ValidationException {


    protected SendTargetMessageVariableMissingException(String messagePrefix, SendTargetErrorCode errorCode) {
        super(messagePrefix, errorCode);
    }

    public static SendTargetMessageVariableMissingException of(String targetKey, String key) {
        return new SendTargetMessageVariableMissingException("[%s][%s] ".formatted(targetKey, key), SendTargetErrorCode.TARGET_VARIABLE_REQUIRED);
    }
}
