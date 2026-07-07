package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public abstract class TargetUploadPolicyViolationException extends PolicyViolationException {
    protected TargetUploadPolicyViolationException(String message) {
        super(message);
    }
}
