package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.domain.exception.target.SendTargetException;

public abstract class TargetUploadException extends SendTargetException {
    protected TargetUploadException(String message) {
        super(message);
    }

}
