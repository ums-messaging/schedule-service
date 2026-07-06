package com.ums.schedule.domain.sendrequest.exception;

public class DefaultRetryCountNotConfiguredException extends SendRequestPolicyException {
    protected DefaultRetryCountNotConfiguredException(String message) {
        super(message);
    }

    public static DefaultRetryCountNotConfiguredException of() {
        return new DefaultRetryCountNotConfiguredException("재시도 횟수를 가져오는데 실패했습니다. ");
    }
}
