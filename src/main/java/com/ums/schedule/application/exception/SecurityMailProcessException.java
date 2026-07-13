package com.ums.schedule.application.exception;

public class SecurityMailProcessException extends ApplicationException {
    protected SecurityMailProcessException(String message) {
        super(message);
    }

    public static SecurityMailProcessException of() {
        return new SecurityMailProcessException("보안 메일 생성 중 오류가 발생했습니다.");
    }
}
