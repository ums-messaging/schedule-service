package com.ums.schedule.application.target.exception;

import com.ums.schedule.common.exception.DbNotFoundException;

import java.util.UUID;

public class TargetUploadReportNotFoundException extends DbNotFoundException {
    protected TargetUploadReportNotFoundException(Object id) {
        super(id);
    }

    public static TargetUploadReportNotFoundException of(UUID id) {
        return new TargetUploadReportNotFoundException(id);
    }
}
