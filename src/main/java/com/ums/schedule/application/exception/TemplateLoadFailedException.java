package com.ums.schedule.application.exception;

import com.ums.schedule.domain.sendrequest.template.exception.TemplatePolicyViolationException;

public class TemplateLoadFailedException extends ApplicationException {

    protected TemplateLoadFailedException(Throwable e) {
        super("템플릿 처리 중 오류가 발생했습니다. ", e);
    }

    public static TemplateLoadFailedException of(Throwable e) {
        return new TemplateLoadFailedException(e);
    }
}
