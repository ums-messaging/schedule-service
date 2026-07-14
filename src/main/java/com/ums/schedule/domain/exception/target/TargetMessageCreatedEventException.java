package com.ums.schedule.domain.exception.target;

import com.ums.schedule.domain.exception.target_upload.TargetUploadEventException;

public class TargetMessageCreatedEventException extends TargetUploadEventException {
    protected TargetMessageCreatedEventException(String message) {
        super(message);
    }

    public static TargetMessageCreatedEventException of() {
        return new TargetMessageCreatedEventException("메시지 ");
    }

    public static TargetMessageCreatedEventException ofTemplate() {
        return new TargetMessageCreatedEventException("메시지 (Template Is Null) ");
    }
}
