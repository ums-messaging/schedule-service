package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.common.exception.InvalidStateException;
import com.ums.schedule.common.code.target_upload.TargetUploadEventEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;

public class InvalidTargetUploadReportStateException extends InvalidStateException {
    protected InvalidTargetUploadReportStateException(String from, String to) {
        super(from, to);
    }

    public static InvalidTargetUploadReportStateException of(TargetUploadStatusEnum from, TargetUploadEventEnum to) {
        return new InvalidTargetUploadReportStateException(from.value(), to.value());
    }
}
