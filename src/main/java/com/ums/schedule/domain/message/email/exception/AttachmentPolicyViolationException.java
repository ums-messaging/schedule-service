package com.ums.schedule.domain.message.email.exception;

import com.ums.schedule.common.code.api.AttachmentErrorCode;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.exception.PolicyViolationException;

public class AttachmentPolicyViolationException extends PolicyViolationException {
    protected AttachmentPolicyViolationException(AttachmentErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static AttachmentPolicyViolationException of(AttachmentErrorCode errorCode) {
        return new AttachmentPolicyViolationException(errorCode);
    }

    public static AttachmentPolicyViolationException of(AttachmentType type, ConvertType convertType) {
        return new AttachmentPolicyViolationException(AttachmentErrorCode.INVALID_FILE_INFO_FORMAT, type, convertType);
    }
}
