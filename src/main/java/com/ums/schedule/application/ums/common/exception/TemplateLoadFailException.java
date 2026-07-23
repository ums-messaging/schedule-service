package com.ums.schedule.application.ums.common.exception;

import com.ums.schedule.common.code.api.TemplateErrorCode;
import com.ums.schedule.common.exception.ExternalSystemException;

public class TemplateLoadFailException extends ExternalSystemException {
    private TemplateLoadFailException(String templateKey, TemplateErrorCode errorCode, Object... args) {
        super(templateKey, errorCode, args);
    }

    public static TemplateLoadFailException of(String templateKey, Throwable e) {
        return new TemplateLoadFailException(templateKey, TemplateErrorCode.TEMPLATE_LOAD_FAIL, e.getMessage());
    }

    public static TemplateLoadFailException of(String templateKey, TemplateErrorCode errorCode, String fileKey) {
        return new TemplateLoadFailException(templateKey, errorCode, fileKey);
    }
}
