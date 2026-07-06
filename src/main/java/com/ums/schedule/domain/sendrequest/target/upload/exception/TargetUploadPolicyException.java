package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class TargetUploadPolicyException extends PolicyViolationException {
    protected TargetUploadPolicyException(String message) {
        super(message);
    }
}
