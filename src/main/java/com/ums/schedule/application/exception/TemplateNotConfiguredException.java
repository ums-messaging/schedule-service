package com.ums.schedule.application.exception;

import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplatePathTypeEnum;

public class TemplateNotConfiguredException extends ApplicationException {
    protected TemplateNotConfiguredException(String message) {
        super(message);
    }

    public static TemplateNotConfiguredException of(String templateKey, EmailTemplatePathTypeEnum pathType) {
        return new TemplateNotConfiguredException("[%s] %s 경로가 설정되어 있지 않습니다.".formatted(templateKey, pathType.description()));
    }
}
