package com.ums.schedule.domain.target.upload.exception;

import com.ums.schedule.common.converter.StatusStateType;
import com.ums.schedule.common.exception.StateException;

public class InvalidTargetUploadStateException extends StateException {
    protected InvalidTargetUploadStateException(StatusStateType from, StatusStateType to) {
        super(from, to);
    }

    public static InvalidTargetUploadStateException of(StatusStateType from, StatusStateType to) {
        return new InvalidTargetUploadStateException(from, to);
    }
}
