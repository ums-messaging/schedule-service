package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public class InvalidTargetUploadReportMismatchException extends PolicyViolationException {
    protected InvalidTargetUploadReportMismatchException(String message) {
        super(message);
    }

    public static InvalidTargetUploadReportMismatchException of(Long currentId, Long uploadId) {
        String message = "Current target upload id is %d. (this id is %d).".formatted(currentId, uploadId);
        return new InvalidTargetUploadReportMismatchException(message);
    }
}
