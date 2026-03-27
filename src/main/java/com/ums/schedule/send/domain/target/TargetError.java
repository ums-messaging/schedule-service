package com.ums.schedule.send.domain.target;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.code.TargetErrorEnum;

public class TargetError {
    private TargetErrorEnum errorCode;
    private String errorMessage;

    public static TargetError of(EnumMapperValue error) {
        TargetErrorEnum targetError = TargetErrorEnum.valueOf(error.code());
        return new TargetError(targetError, error.description());
    }
    public static TargetError ofErrorMessage(String message) {
        return new TargetError(TargetErrorEnum.ETC, message);
    }

    private TargetError(TargetErrorEnum errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
