package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.common.exception.InvalidStateException;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadEventEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadStatusEnum;

public class InvalidTargetUploadReportStateException extends InvalidStateException {
    protected InvalidTargetUploadReportStateException(String from, String to) {
        super(from, to);
    }

    public static InvalidTargetUploadReportStateException of(TargetUploadStatusEnum from, TargetUploadEventEnum to) {
        return new InvalidTargetUploadReportStateException(from.value(), to.value());
    }
}
