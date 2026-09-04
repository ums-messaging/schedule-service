package com.ums.schedule.domain.message.email.exception;

import com.ums.schedule.common.code.api.AttachmentErrorCode;
import com.ums.schedule.common.exception.BusinessException;

public class AttachmentPolicyViolationException extends BusinessException {
    protected AttachmentPolicyViolationException(AttachmentErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static AttachmentPolicyViolationException of(AttachmentErrorCode errorCode) {
        return new AttachmentPolicyViolationException(errorCode);
    }
}
