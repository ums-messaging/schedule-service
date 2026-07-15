package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.domain.exception.PolicyViolationException;

public abstract class TargetUploadPolicyViolationException extends PolicyViolationException {
    protected TargetUploadPolicyViolationException(String message) {
        super(message);
    }
}
