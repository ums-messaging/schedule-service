package com.ums.schedule.application.exception;

public class ConvertMessageNotConfiguredException extends ApplicationException {
    protected ConvertMessageNotConfiguredException(String field) {
        super("[%s] 데이터를 조회하는데 실패했습니다. ".formatted(field));
    }

    public static ConvertMessageNotConfiguredException of(String field) {
        return new ConvertMessageNotConfiguredException(field);
    }
}
