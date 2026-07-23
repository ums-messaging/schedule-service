package com.ums.schedule.application.ums.common.exception;

import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.TemplateErrorCode;
import com.ums.schedule.common.code.target.TargetColumnEnum;
import com.ums.schedule.common.exception.PolicyViolationException;

public class TemplateParseException extends PolicyViolationException {
    protected TemplateParseException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
    protected TemplateParseException(String id, ErrorCode errorCode, Object... args) {
        super(id, errorCode, args);
    }

    public static TemplateParseException of(String targetKey, String templateKey) {
        return new TemplateParseException(TemplateErrorCode.TEMPLATE_PARSE_FAIL, targetKey, templateKey);
    }

    public static TemplateParseException of(String id, Throwable e) {
        return new TemplateParseException(id, TemplateErrorCode.TEMPLATE_PARSE_FAIL, e.getMessage());
    }

}
