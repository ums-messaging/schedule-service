package com.ums.schedule.domain.request.exception;

public class InvalidScheduleException extends SendRequestException {

    protected InvalidScheduleException(String message) {
        super(message);
    }

    public static InvalidScheduleException invalidSchedule() {
        return new InvalidScheduleException("현재 시각이 스케줄 허용 시간 범위에 포함되지 않거나, 실행 중인 스케쥴이 아닙니다.");
    }
}
