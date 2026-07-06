package com.ums.schedule.domain.schedule.exception;

public class ScheduleNotExecutableException extends SchedulePolicyViolationException {
    protected ScheduleNotExecutableException(String act) {
        super("schedule is %s.".formatted(act));
    }

    public static ScheduleNotExecutableException expiredOf() {
        return new ScheduleNotExecutableException("expired");
    }

    public static ScheduleNotExecutableException notRunningOf() {
        return new ScheduleNotExecutableException("not running");
    }

    public static ScheduleNotExecutableException inActiveOf() {
        return new ScheduleNotExecutableException("inactive");
    }
}
