package com.ums.schedule.domain.target.upload.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.exception.PolicyViolationException;

public class TargetUploadPolicyViolationException extends PolicyViolationException {
    protected TargetUploadPolicyViolationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static TargetUploadPolicyViolationException of(TargetUploadErrorCode errorCode, Object... args) {
        return new TargetUploadPolicyViolationException(errorCode, args);
    }

}
