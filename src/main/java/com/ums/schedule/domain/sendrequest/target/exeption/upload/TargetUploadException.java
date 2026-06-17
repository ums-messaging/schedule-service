package com.ums.schedule.domain.sendrequest.target.exeption.upload;

import com.ums.schedule.domain.sendrequest.target.exeption.SendTargetException;

public abstract class TargetUploadException extends SendTargetException {
    protected TargetUploadException(String message) {
        super(message);
    }

}
