package com.ums.schedule.application.ums.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.TemplateErrorCode;
import com.ums.schedule.common.exception.PolicyViolationException;

public class TemplatePolicyViolationException extends PolicyViolationException {
    protected TemplatePolicyViolationException(Object id, ErrorCode errorCode, Object... args) {
        super(id, errorCode, args);
    }

    protected TemplatePolicyViolationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public static TemplatePolicyViolationException of(String templateKey, Object... args) {
        return new TemplatePolicyViolationException(templateKey, TemplateErrorCode.TEMPLATE_CONTENT_EMPTY, args);
    }
}
