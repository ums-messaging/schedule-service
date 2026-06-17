package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.domain.sendrequest.target.exeption.upload.TargetUploadException;

public abstract class TargetUploadReportCreateException extends TargetUploadException {

    protected TargetUploadReportCreateException(String message) {
        super(message);
    }
}
