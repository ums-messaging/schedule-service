package com.ums.schedule.domain.sendrequest.target.exeption;

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
