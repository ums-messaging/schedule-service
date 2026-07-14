package com.ums.schedule.application.exception.target_upload;

import com.ums.schedule.application.exception.ApplicationException;
import com.ums.schedule.application.exception.common.NotSupportedException;

public class TargetUploadFormatNotSupportedException extends NotSupportedException {

    protected TargetUploadFormatNotSupportedException(Throwable cause) {
        super(cause);
    }

    public static TargetUploadFormatNotSupportedException of(Throwable e) {
        return new TargetUploadFormatNotSupportedException(e);
    }
}
