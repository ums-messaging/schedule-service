package com.ums.schedule.application.exception.template;

import com.ums.schedule.application.exception.ApplicationException;

public class TemplateKeyTemplateNotExistException extends ApplicationException {
    protected TemplateKeyTemplateNotExistException(String message) {
        super(message);
    }

    public static  TemplateKeyTemplateNotExistException of() {
        return new TemplateKeyTemplateNotExistException("파일 키, 파일 템플릿 키를 불러오는데 실패했습니다.");
    }
}
