package com.ums.schedule.domain.exception.schedule;

public class InvalidScheduleStatusException  extends  ScheduleDomainException {
    protected InvalidScheduleStatusException(String message) {
        super(message);
    }


    public static InvalidScheduleStatusException changeToStatus(String from, String to) {
        String message = String.format("%s에서 %s 상태로 변경 불가합니다.", from, to);
        return new InvalidScheduleStatusException(message);
    }

    public static InvalidScheduleStatusException notRunning() {
        return new InvalidScheduleStatusException("실행 중인 스케쥴이 아닙니다.");
    }

    public static InvalidScheduleStatusException invalidStatus() {
        return new InvalidScheduleStatusException("실행 중인 상태에서는 변경할 수 없습니다.");
    }

    public static InvalidScheduleStatusException inActiveSchedule() {
        return new InvalidScheduleStatusException("비활성화 된 스케쥴입니다.");
    }
}
