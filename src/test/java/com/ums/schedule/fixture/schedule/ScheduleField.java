package com.ums.schedule.fixture.schedule;

public enum ScheduleField {
    SCHEDULE_NAME("name"),
    SCHEDULE_TYPE("scheduleType"),
    STATUS("status"),
    CYCLE_POLICY("cyclePolicy"),
    CYCLE_CD("cycleCd"),
    SCHEDULE_PERIOD("schedulePeriod"),
    SCHEDULE_START_AT("scheduleStartAt"),
    SCHEDULE_END_AT("scheduleEndAt"),
    CREATED_BY("createdBy"),
    CREATED_AT("createdAt")

    ;
    String field;
    ScheduleField(String field) {
        this.field = field;
    }

    public String value() {
        return this.field;
    }
}
