package com.ums.schedule.application.exception;

public class TargetUploadFormatNotSupportedException extends ApplicationException {
    protected TargetUploadFormatNotSupportedException(String message) {
        super(message);
    }

    protected TargetUploadFormatNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    protected TargetUploadFormatNotSupportedException(Throwable cause) {
        super(cause);
    }

    public static TargetUploadFormatNotSupportedException of(Throwable e) {
        return new TargetUploadFormatNotSupportedException(e);
    }
}
