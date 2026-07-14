package com.ums.schedule.application.exception.template;

import com.ums.schedule.application.exception.ApplicationException;
import com.ums.schedule.application.exception.common.BusinessException;

public class TemplateLoadFailedException extends BusinessException {

    protected TemplateLoadFailedException(Throwable e) {
        super("템플릿 처리 중 오류가 발생했습니다. ");
    }

    public static TemplateLoadFailedException of(Throwable e) {
        return new TemplateLoadFailedException(e);
    }
}
