package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

import java.util.UUID;

public class InvalidTargetUploadReportMismatchException extends PolicyViolationException {
    protected InvalidTargetUploadReportMismatchException(String message) {
        super(message);
    }

    public static InvalidTargetUploadReportMismatchException of(UUID currentId, UUID uploadId) {
        String message = "Current target upload id is %s. (this id is %s).".formatted(currentId.toString(), uploadId.toString());
        return new InvalidTargetUploadReportMismatchException(message);
    }
}
