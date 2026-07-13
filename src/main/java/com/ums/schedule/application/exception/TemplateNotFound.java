package com.ums.schedule.application.exception;

public class TemplateNotFound extends ApplicationException {
    protected TemplateNotFound(String message) {
        super(message);
    }

    public static TemplateNotFound of() {
        return new TemplateNotFound("바디 템플릿이 존재하지 않습니다.");
    }
}
