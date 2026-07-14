package com.ums.schedule.application.exception.template;

import com.ums.schedule.application.exception.common.NotConfiguredException;
import com.ums.schedule.common.code.email.EmailTemplatePathTypeEnum;

public class TemplateNotConfiguredException extends NotConfiguredException {
    protected TemplateNotConfiguredException(String message) {
        super(message);
    }

    public static TemplateNotConfiguredException of(String templateKey, EmailTemplatePathTypeEnum pathType) {
        return new TemplateNotConfiguredException("[%s] %s 경로가 설정되어 있지 않습니다.".formatted(templateKey, pathType.description()));
    }
}
