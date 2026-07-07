package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.common.exception.DomainException;

public abstract class TargetUploadReportException extends DomainException {
    protected TargetUploadReportException(String message) {
        super(message);
    }

    protected TargetUploadReportException(String message, Throwable e) {
        super(message, e);
    }

    protected TargetUploadReportException(Throwable e) {
        super(e);
    }
}
