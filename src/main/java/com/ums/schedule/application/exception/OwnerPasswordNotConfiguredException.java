package com.ums.schedule.application.exception;

public class OwnerPasswordNotConfiguredException extends ApplicationException {
    protected OwnerPasswordNotConfiguredException(String message) {
        super(message);
    }

    public static OwnerPasswordNotConfiguredException of() {
        return new OwnerPasswordNotConfiguredException("owner password를 가져오는데 실패했습니다.");
    }
}
